package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * The #null operand.
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class NullOperand extends DVal {

    @Override
    public String toString() {
        return "#null";
    }

    @Override
    public GPRegister codeGenToGPRegister(DecacCompiler compiler) {
        GPRegister reg = compiler.allocGPRegister();
        compiler.addInstruction(new LOAD(this, reg));
        return reg;
    }

    @Override
    public void freeGPRegister(DecacCompiler compiler) {
        // Do nothing
    }

}
