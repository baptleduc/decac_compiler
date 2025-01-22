package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;

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
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        return new BGE(setTrueLabel);
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected String getARMCmpInverseAcronym() {
        return "lt";
    }

}
