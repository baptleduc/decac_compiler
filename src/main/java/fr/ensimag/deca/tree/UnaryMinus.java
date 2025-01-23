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
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type operandType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (operandType.isFloat()) {
            setType(operandType);
            return operandType;
        } else if (operandType.isInt()) {
            setType(operandType);
            return operandType;
        }
        throw new ContextualError(
                "Var " + operandType.getName() + " can't be used for 'UnaryMinus'",
                this.getOperand().getLocation());
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenUnaryExpr(GPRegister regDest, DVal sourceDVal, DecacCompiler compiler) {
        compiler.addInstruction(new OPP(sourceDVal, regDest));
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
