package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(Divide.class);
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler) {
        LOG.debug("DIV: " + left + "," + right);
        if (getLeftOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(right, left));
        }
        else {
            compiler.addInstruction(new DIV(right, left));
        }
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }
}
