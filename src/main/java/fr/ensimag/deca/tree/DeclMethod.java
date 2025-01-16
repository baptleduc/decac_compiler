package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclMethod extends AbstractDeclMethod {

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
    protected EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superClass, int index)
            throws ContextualError {
        Type methodType = returnType.verifyType(compiler);
        Signature signature = params.verifyListParams(compiler);
        EnvironmentExp envExpSuper = superClass.getClassDefinition().getMembers();
        Type type1 = compiler.environmentType.getEnvTypes().get(returnType.getName()).getType();
        ExpDefinition superClassDefinition = envExpSuper.getCurrentEnvironment().get(methodName.getName());
        MethodDefinition defMethod;
        MethodDefinition methodSuperClass;
        Signature sign2;
        Type type2;

        if (envExpSuper.getCurrentEnvironment().containsKey(methodName.getName())) {
            methodSuperClass = superClassDefinition.asMethodDefinition("is not a method definition",
                    methodName.getLocation());
            sign2 = methodSuperClass.getSignature();
            type2 = methodSuperClass.getType();

            if ((signature.sameSign(sign2))
                    && (type1.sameType(type2) || type1.asClassType("not a class Type", methodName.getLocation())
                            .isSubClassOf(type2.asClassType("not a class Type", methodName.getLocation())))) {
                // Override Condition
                defMethod = new MethodDefinition(type1, methodName.getLocation(), signature, index);
            } else {
                throw new ContextualError(methodName.getName() + " can not be overloaded", methodName.getLocation());
            }
        }
        defMethod = new MethodDefinition(type1, methodName.getLocation(), signature, index);

        EnvironmentExp environmentMethod = new EnvironmentExp(null);
        try {
            environmentMethod.declare(methodName.getName(), defMethod);
        } catch (Exception e) {
            // do nothing
        }
        returnType.setType(methodType);
        methodName.setDefinition(defMethod);
        return environmentMethod;
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

}
