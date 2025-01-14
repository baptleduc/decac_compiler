package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler) {
        compiler.addInstruction(new MUL(right, left));
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, GPRegister reg, boolean branchOnTrue, Label branchLabel) {
        throw new DecacInternalError("Should not be called");
    }

}
