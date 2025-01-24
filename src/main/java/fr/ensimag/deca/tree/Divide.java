package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
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
    protected void codeGenOperationInstARM(String dest, String left, AbstractExpr right, DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        String rightRg;
        if (right.isImmediate()) {
            rightRg = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", rightRg, right.getARMDVal().toString()));
        } else {
            rightRg = right.getARMDVal().toString();
        }
        compiler.getARMProgram().addInstruction(new ARMInstruction("sdiv", dest, left, rightRg));

        if (right.isImmediate()) {
            program.freeRegister(rightRg);
        }
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

    @Override
    protected void addFloatOpARM(ARMProgram prog, String lr, String rr) {
        prog.addInstruction(new ARMInstruction("fdiv", lr, lr, rr));
    }
}
