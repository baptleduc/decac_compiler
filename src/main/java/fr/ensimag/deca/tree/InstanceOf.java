package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author nicolmal
 * @date 06/01/2025
 */
public class InstanceOf extends AbstractExpr {

    private AbstractExpr leftOperand;
    private AbstractIdentifier rightOperand;

    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type rightType = rightOperand.verifyType(compiler);
        if((leftType.isClass()||leftType.isNull())&&rightType.isClass()){
            leftOperand.setType(leftType);
            rightOperand.setType(rightType);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError("Can't use instanceof with this type "+rightOperand.getName(),rightOperand.getLocation());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        leftOperand.decompile(s);
        s.print(" instanceof ");
        rightOperand.decompile(s);
        s.print(")");
    }

    @Override
    String prettyPrintNode() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
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
