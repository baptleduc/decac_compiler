package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler) {
        compiler.addInstruction(new SUB(right, left));
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void optimizeLeftImmediate(DecacCompiler compiler, DVal leftDVal, DVal rightDVal) {
        // Minus does not have a commutative property so we cannot optimize
        GPRegister regDest = leftDVal.codeGenToGPRegister(compiler);
        DVal sourceDVal = rightDVal.codeGenToGPRegister(compiler);
        setRegDest(regDest);
        setSourceDVal(sourceDVal);
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
