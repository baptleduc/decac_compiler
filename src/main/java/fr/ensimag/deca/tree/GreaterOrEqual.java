package fr.ensimag.deca.tree;

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
    protected boolean isImmediate() {
        return false;
    }

}
