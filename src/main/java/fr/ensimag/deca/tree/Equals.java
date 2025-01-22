package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        return new BEQ(setTrueLabel);
    }

    @Override
    protected String getARMCmpInverseAcronym() {
        return "ne";
    }

}
