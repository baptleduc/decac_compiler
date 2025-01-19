package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler) {
        compiler.addInstruction(new MUL(right, left));
    }

    @Override
    protected void codeGenOperationInstARM(String dest, String left, AbstractExpr right, DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        String rightRg;
        if (right.isImmediate()){
            rightRg = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", rightRg, right.getARMDVal().toString()));
        }
        else{
            rightRg = right.getARMDVal().toString();
        }
        compiler.getARMProgram().addInstruction(new ARMInstruction("mul", dest, left, rightRg));
        if (right.isImmediate()){
            program.freeRegister(rightRg);
        }
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

}
