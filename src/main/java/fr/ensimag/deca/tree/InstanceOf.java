package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;

/**
 *
 * @author nicolmal
 * @date 06/01/2025
 */
public class InstanceOf extends AbstractOpExactCmp {

    public InstanceOf(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }
    
    @Override
    protected BranchInstruction getBranchInstruction(Label setTrueLabel) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
