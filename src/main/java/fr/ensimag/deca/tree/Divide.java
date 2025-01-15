package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import org.apache.log4j.Logger;

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
        } else {
            compiler.addInstruction(new DIV(right, left));
        }

        compiler.addInstruction(new BOV(LabelManager.DIVIDE_BY_ZERO_ERROR.getLabel()));
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void optimizeLeftImmediate(DecacCompiler compiler, DVal leftDVal, DVal rightDVal) {
        // Divide does not have a commutative property so we cannot optimize
        GPRegister regDest = leftDVal.codeGenToGPRegister(compiler);
        DVal sourceDVal = rightDVal.codeGenToGPRegister(compiler);
        setRegDest(regDest);
        setSourceDVal(sourceDVal);
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
