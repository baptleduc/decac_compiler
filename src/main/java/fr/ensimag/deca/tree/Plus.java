package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import org.apache.log4j.Logger;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Plus extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(Plus.class);

    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler) {
        LOG.debug("ADD:  " + left + "," + right);
        compiler.addInstruction(new ADD(right, left));
    }

    @Override
    protected void codeGenOperationInstARM(String dest, String left, AbstractExpr right, DecacCompiler compiler) {
        compiler.getARMProgram().addInstruction(new ARMInstruction("add", dest, left, right.getARMDVal().toString()));
    }

    @Override
    protected void addFloatOpARM(ARMProgram prog, String lr, String rr) {
        prog.addInstruction(new ARMInstruction("fadd", lr, lr, rr));
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }
}
