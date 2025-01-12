package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

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
        throw new UnsupportedOperationException("Not supposed to be called");
    }

    @Override
    public void free(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supposed to be called");
    }

}
