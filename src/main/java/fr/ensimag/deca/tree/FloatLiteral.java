package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl12
 * @date 01/01/2025
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;
    private ImmediateFloat immediate;

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
        this.immediate = new ImmediateFloat(value);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Retrieve the 'float' type from the predefined environment
        Type floatType = compiler.environmentType.FLOAT;

        // Decorate the node with the 'float' type
        this.setType(floatType);

        // Return the type of the expression
        return floatType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        return immediate;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        setDVal(immediate);
    }

    @Override
    protected boolean isImmediate() {
        return true;
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();
        String reg = program.getAvailableRegister();
        String floatReg = program.getAvailableRegisterTypeS();

        int intBitsRepresentation = Float.floatToIntBits(value);
        int lower16 = intBitsRepresentation & 0xFFFF;
        int upper16 = (intBitsRepresentation >>> 16) & 0xFFFF;

        program.addInstruction(new ARMInstruction("mov", reg, lower16));
        program.addInstruction(new ARMInstruction("movk", reg, "#" + upper16, "lsl #16"));
        program.addInstruction(new ARMInstruction("fmov", floatReg, reg));

        program.freeRegister(reg);

        setARMDVal(new ARMDVal(floatReg));
    }
}
