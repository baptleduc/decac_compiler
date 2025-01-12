package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import org.apache.log4j.Logger;

/**
 * Register operand (including special registers like SP).
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public class Register extends DVal {
    private static final Logger LOG = Logger.getLogger(Register.class);
    private String name;
    private static final int MAX_GP_REGISTERS = 16;

    protected Register(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public GPRegister codeGenToGPRegister(DecacCompiler compiler) {
        throw new DecacInternalError("Should not be called");
    }

    public void free(DecacCompiler compiler) {
        throw new DecacInternalError("Should not be called");
    }

    /**
     * Global Base register
     */
    public static final Register GB = new Register("GB");
    /**
     * Local Base register
     */
    public static final Register LB = new Register("LB");
    /**
     * Stack Pointer
     */
    public static final Register SP = new Register("SP");
    /**
     * General Purpose Registers. Array is private because Java arrays cannot be
     * made immutable, use getR(i) to access it.
     */
    private static final GPRegister[] R = initRegisters();

    /**
     * General Purpose Registers
     */
    public static GPRegister getR(int i) {
        return R[i];
    }

    public static int getMaxGPRegisters() {
        return MAX_GP_REGISTERS;
    }

    /**
     * Convenience shortcut for R[0]
     */
    public static final GPRegister R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final GPRegister R1 = R[1];

    static private GPRegister[] initRegisters() {
        GPRegister[] res = new GPRegister[MAX_GP_REGISTERS];
        for (int i = 0; i < MAX_GP_REGISTERS; i++) {
            res[i] = new GPRegister("R" + i, i);
        }
        return res;
    }
}
