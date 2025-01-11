package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLT;

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
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        return new BLT(setTrueLabel);
    }

}
