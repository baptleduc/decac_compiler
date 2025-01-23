package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl12
 * @date 01/01/2025
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        setType(compiler.environmentType.FLOAT);
        return compiler.environmentType.FLOAT;
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.decompile(s);
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenUnaryExpr(GPRegister regDest, DVal sourceDVal, DecacCompiler compiler) {
        compiler.addInstruction(new FLOAT(sourceDVal, regDest));
    }

    @Override
    protected String codeGenUnaryExprARM(ARMDVal dval, ARMProgram program) {

        String srcReg = program.getReadyRegister(dval);
        String sreg = program.getAvailableRegisterTypeS();
        program.addInstruction(new ARMInstruction("scvtf", sreg, srcReg));
        program.freeRegister(srcReg);
        return sreg;
    }
}
