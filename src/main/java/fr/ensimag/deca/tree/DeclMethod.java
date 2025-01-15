package fr.ensimag.deca.tree;

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
    // @Override
    // protected void verifyMethod(DecacCompiler compiler, int index) throws
    // ContextualError {
    // Type methodType = returnType.verifyType(compiler);
    // Signature signature = params.verifyListParams(compiler);
    // EnvironmentExp envExpSuper = superClass.getClassDefinition().getMembers();
    // if(envExpSuper.currentEnvironment.containsKey(methodName.getName())){
    // if(envExpSuper(methodName.getName()).getSignature() != signature){
    // throw new ContextualError("Method"+ var.getName()+ "must have the same sig
    // that inherited method", var.getLocation());
    // }
    // //else if(envExpSuper(methodName.getName()).getType() not subtype of
    // returnType))
    // }
    // MethodDefinition defMethod = new MethodDefinition(returnType,
    // methodName.getLocation(), signature, index+1);
    // EnvironmentExp environmentMethod = new EnvironmentExp();
    // environmentMethod.set(methodName.getName(), defMethod);
    // return environmentMethod;
    // }

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
