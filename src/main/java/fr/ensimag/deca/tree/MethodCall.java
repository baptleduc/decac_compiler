package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodCall extends AbstractExpr {

    private AbstractExpr leftOperand;
    private AbstractIdentifier rightOperand;
    private ListExpr params;

    public MethodCall(AbstractExpr leftOperand, AbstractIdentifier rightOperand, ListExpr params) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        ClassType classType2 = leftOperand.verifyExpr(compiler, localEnv, currentClass).asClassType(
                "method " + rightOperand.getName() + " can only be called on class types", leftOperand.getLocation());
        ClassDefinition classDef2 = classType2.getDefinition();
        EnvironmentExp envExp2 = classDef2.getMembers();
        MethodDefinition methodDef = rightOperand.verifyIdentifier(localEnv)
                .asMethodDefinition(rightOperand.getName() + " is not defined as a method", rightOperand.getLocation());
        Signature sig = methodDef.getSignature();
        for (AbstractExpr param : params.getList()) {
            param.verifyRValue(compiler, localEnv, currentClass, param.getType());
        }
        return methodDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    String prettyPrintNode() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected boolean isImmediate() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
