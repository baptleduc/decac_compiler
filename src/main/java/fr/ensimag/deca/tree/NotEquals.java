package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler,GPRegister regDest, boolean branchOnTrue, Label branchLabel) {
        if (branchOnTrue) {
            compiler.addInstruction(new BNE(branchLabel));
        } else {
            compiler.addInstruction(new BEQ(branchLabel));
        }
    }

}
