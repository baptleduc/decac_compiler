package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;

/**
 *
 * @author nicolmal
 * @date 06/01/2025
 */
public class InstanceOf extends AbstractOpIneq {

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

}
