package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(DeclMethod.class);
    private AbstractIdentifier returnType;
    private AbstractIdentifier methodName;
    private ListDeclParam params;
    private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam params,
            AbstractMethodBody body) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.params = params;
        this.body = body;
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    @Override
    protected EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier classIdentifier,
            AbstractIdentifier superClass)
            throws ContextualError {
        Type methodType = returnType.verifyType(compiler);
        Signature signature = params.verifyListParams(compiler);
        EnvironmentExp envExpSuper = superClass.getClassDefinition().getMembers();
        Type type1 = compiler.environmentType.getEnvTypes().get(returnType.getName()).getType();
        ExpDefinition superClassDefinition = envExpSuper.getCurrentEnvironment().get(methodName.getName());
        MethodDefinition defMethod;
        ClassDefinition defClass = (ClassDefinition) (compiler.environmentType.getEnvTypes()
                .get(classIdentifier.getName()));
        MethodDefinition methodSuperClass;
        Signature sign2;
        Type type2;
        LOG.debug("Number of methods of " + classIdentifier.getName() + " : " + defClass.getNumberOfMethods());
        int indexMethod = defClass.getNumberOfMethods();

        if (envExpSuper.getCurrentEnvironment().containsKey(methodName.getName())) {
            methodSuperClass = superClassDefinition.asMethodDefinition("is not a method definition",
                    methodName.getLocation());
            sign2 = methodSuperClass.getSignature();
            type2 = methodSuperClass.getType();

            if ((signature.sameSign(sign2))
                    && (type1.sameType(type2) || type1.asClassType("not a class Type", methodName.getLocation())
                            .isSubClassOf(type2.asClassType("not a class Type", methodName.getLocation())))) {
                // Override Condition
                indexMethod = methodSuperClass.getIndex();
            } else {
                throw new ContextualError(methodName.getName() + " can not be overloaded", methodName.getLocation());
            }
        } else {
            defClass.incNumberOfMethods();
            LOG.debug("inc Number of methods");
        }
        defMethod = new MethodDefinition(type1, methodName.getLocation(), signature, indexMethod);

        EnvironmentExp environmentMethod = new EnvironmentExp(null);
        try {
            environmentMethod.declare(methodName.getName(), defMethod);
        } catch (Exception e) {
            // do nothing
        }
        returnType.setType(methodType);
        methodName.setDefinition(defMethod);
        LOG.debug("Method " + methodName.getName() + " index: " + defMethod.getIndex());
        return environmentMethod;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, AbstractIdentifier methodClass)
            throws ContextualError {
        Type methodReturnType = returnType.verifyType(compiler);
        EnvironmentExp envExpParams = params.verifyListParamsBody(compiler);
        body.verifyMethodBodyBody(compiler, envExp, envExpParams, methodClass, methodReturnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        returnType.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(")");
        body.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnType.iter(f);
        methodName.iter(f);
        params.iter(f);
        body.iter(f);
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        Label methodLable = methodName.getMethodDefinition().getLabel();
        compiler.addLabel(methodLable);
        body.codeGenMethodBody(compiler);
    }
}
