package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        return new BGT(setTrueLabel);
    }

}
