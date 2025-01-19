package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!rightType.isInt()) {
            throw new ContextualError(
                    "Var" + rightType.getName() + " it not an int : modulo impossible",
                    this.getRightOperand().getLocation());
        } else if (!leftType.isInt()) {
            throw new ContextualError(
                    "Var" + leftType.getName() + " it not an int : modulo impossible",
                    this.getLeftOperand().getLocation());
        }
        setType(compiler.environmentType.INT);
        return compiler.environmentType.INT;
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler) {
        compiler.addInstruction(new REM(right, left));
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
        String tmpReg = program.getAvailableRegister();
        program.addInstruction(new ARMInstruction("udiv", tmpReg, left, rightRg));
        program.addInstruction(new ARMInstruction("mul", tmpReg, tmpReg, rightRg));
        program.addInstruction(new ARMInstruction("sub", dest, left, tmpReg));
        program.freeRegister(tmpReg);
        if (right.isImmediate()){
            program.freeRegister(rightRg);
        }
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

}
