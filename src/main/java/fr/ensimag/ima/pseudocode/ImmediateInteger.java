package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public class ImmediateInteger extends DVal {
    private static final Logger LOG = Logger.getLogger(ImmediateInteger.class);
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
    }

    @Override
    public GPRegister codeGenToGPRegister(DecacCompiler compiler) {
        GPRegister reg = compiler.allocGPRegister();
        compiler.addInstruction(new LOAD(this, reg));
        LOG.debug("Immediate value " + value + " loaded into register " + reg);
        return reg;
    }

    @Override
    public void free(DecacCompiler compiler) {
        // Do nothing
    }

    @Override
    public String toString() {
        return "#" + value;
    }
}
