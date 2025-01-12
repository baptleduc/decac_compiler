package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Immediate operand containing a float value.
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
        super();
        this.value = value;
    }

    @Override
    public GPRegister codeGenToGPRegister(DecacCompiler compiler) {
        GPRegister reg = compiler.allocGPRegister();
        compiler.addInstruction(new LOAD(this, reg));
        return reg;
    }

    @Override
    public void free(DecacCompiler compiler) {
        // Do nothing
    }

    @Override
    public String toString() {
        return "#" + Float.toHexString(value);
    }
}
