package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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

        if (envExpSuper.getCurrentEnvironment().containsKey(methodName.getName())) {
            if (!methodType.asClassType("this is not a class type", returnType.getLocation())
                    .isSubClassOf(type1.asClassType("this is not a class type", methodName.getLocation()))) {
                throw new ContextualError(
                        "Method" + methodName.getName() + "must have the same type that the inherited method one",
                        methodName.getLocation());
            } else if (envExpSuper.getCurrentEnvironment().get((methodName.getName()))
                    .asMethodDefinition("ce n'est pas une méthode", methodName.getLocation())
                    .getSignature() != signature) {
                throw new ContextualError(
                        "Method" + methodName.getName() + "must have the same signature that inherited method",
                        methodName.getLocation());
            }

        }
        MethodDefinition defMethod = new MethodDefinition(type1, methodName.getLocation(), signature, index);
        EnvironmentExp environmentMethod = new EnvironmentExp(null);
        try {
            environmentMethod.declare(methodName.getName(), defMethod);
        } catch (Exception e) {
            // do nothing
        }

        return environmentMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" ... A FAIRE ... ");
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
        throw new UnsupportedOperationException("Not yet supported");
    }

}
