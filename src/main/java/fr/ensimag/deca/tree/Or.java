package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label setFalse = new Label("set_true");
        codeGenBooleanOperation(compiler, true, setFalse);
    }

    @Override
    protected boolean endIfTrueARM() {
        return true;
    }

}
