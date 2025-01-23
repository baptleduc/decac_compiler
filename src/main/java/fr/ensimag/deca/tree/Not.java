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
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type operandType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!operandType.isBoolean()) {
            throw new ContextualError(
                    "Var " + operandType.getName() + " can't be used for 'Not'",
                    this.getOperand().getLocation());
        }
        setType(operandType);
        return operandType;
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenUnaryExpr(GPRegister regDest, DVal sourceDVal, DecacCompiler compiler) {
        Label endLabel = new Label("end");
        Label setTrueLabel = new Label("set_true");

        // Compare the value of the register to 0
        compiler.addInstruction(new CMP(new ImmediateInteger(0), regDest));
        compiler.addInstruction(new BEQ(setTrueLabel));

        // If the condition is not met, we load false
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), regDest));
        compiler.addInstruction(new BRA(endLabel));

        // If the condition is met, we load true
        compiler.addLabel(setTrueLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), regDest));
        compiler.addInstruction(new BRA(endLabel));

        compiler.addLabel(endLabel);
    }

    @Override
    protected String codeGenUnaryExprARM(ARMDVal dval, ARMProgram program) {

        String srcReg = program.getReadyRegister(dval);

        String minus1 = program.getAvailableRegister();
        program.addInstruction(new ARMInstruction("mov", minus1, "#-1"));
        program.addInstruction(new ARMInstruction("sub", srcReg, srcReg, minus1));
        program.addInstruction(new ARMInstruction("mul", srcReg, srcReg, minus1));
        program.freeRegisterTypeW(minus1);
        return srcReg;
    }
}
