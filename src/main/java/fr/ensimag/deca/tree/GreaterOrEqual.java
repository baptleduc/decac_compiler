package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 * Operator "x >= y"
 * 
 * @author gl12
 * @date 01/01/2025
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler,GPRegister regDest, boolean branchOnTrue, Label branchLabel) {
        if (branchOnTrue) {
            compiler.addInstruction(new BGE(branchLabel));
        } else {
            compiler.addInstruction(new BLT(branchLabel));
        }
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

}
