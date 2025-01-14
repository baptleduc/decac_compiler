package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        Label endLabel = new Label("And_end_label");
        if (branchOn) {
            getLeftOperand().codeGenBool(compiler, endLabel, false);
            getRightOperand().codeGenBool(compiler, label, true);
        } else {
            getLeftOperand().codeGenBool(compiler, label, false);
            getRightOperand().codeGenBool(compiler, label, false);
        }

        compiler.addLabel(endLabel);
    }
}
