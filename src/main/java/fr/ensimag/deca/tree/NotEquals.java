package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
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
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        return new BNE(setTrueLabel);
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

}
