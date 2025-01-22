package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLE;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        return new BLE(setTrueLabel);
    }

    @Override
    protected String getARMCmpInverseAcronym() {
        return "gt";
    }

}
