package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler,GPRegister regDest, boolean branchOnTrue, Label branchLabel) {
        if (branchOnTrue) {
            compiler.addInstruction(new BLT(branchLabel));
        } else {
            compiler.addInstruction(new BGE(branchLabel));
        }
    }

}
