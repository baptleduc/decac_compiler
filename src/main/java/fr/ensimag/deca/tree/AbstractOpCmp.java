package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        if (this instanceof AbstractOpExactCmp) {
            setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        } else if ((rightType.sameType(compiler.environmentType.INT)
                || rightType.sameType(compiler.environmentType.FLOAT))
                && (leftType.sameType(compiler.environmentType.INT)
                        || leftType.sameType(compiler.environmentType.FLOAT))) {
            setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError("Vars " + rightType.getName() + " and " + leftType.getName() + " can't be compared",
                this.getRightOperand().getLocation());
    }

    protected abstract void codeGenBranch(DecacCompiler compiler, Label label, boolean branchOn);

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);
        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        GPRegister regLeft = leftDVal.codeGenToGPRegister(compiler);

        // Compare the two values
        compiler.addInstruction(new CMP(rightDVal, regLeft));
        codeGenBranch(compiler, label, branchOn);
    }
}
