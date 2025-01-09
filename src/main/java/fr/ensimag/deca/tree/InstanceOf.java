package fr.ensimag.deca.tree;

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

}
