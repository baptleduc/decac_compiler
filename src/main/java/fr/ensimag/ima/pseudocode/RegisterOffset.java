package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Operand representing a register indirection with offset, e.g. 42(R3).
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class RegisterOffset extends DAddr {
    public int getOffset() {
        return offset;
    }

    public Register getRegister() {
        return register;
    }

    private final int offset;
    private final Register register;

    public RegisterOffset(int offset, Register register) {
        super();
        this.offset = offset;
        this.register = register;
    }

    @Override
    public GPRegister codeGenToGPRegister(DecacCompiler compiler) {
        GPRegister reg = compiler.allocGPRegister();
        compiler.addInstruction(new LOAD(this, reg));
        return reg;
    }

    @Override
    public void freeGPRegister(DecacCompiler compiler) {
        if(register.isGPRegister()) {
            ((GPRegister) register).freeGPRegister(compiler);
        }
    }
    @Override
    public String toString() {
        return offset + "(" + register + ")";
    }
}
