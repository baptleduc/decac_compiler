package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class And extends AbstractOpBool {
    private static final Logger LOG = Logger.getLogger(And.class);

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label setFalse = new Label("set_false");
        codeGenBooleanOperation(compiler, false, setFalse);
    }

    @Override
    protected boolean endIfTrueARM() {
        return false;
    }

}
