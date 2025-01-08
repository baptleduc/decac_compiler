package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.POP;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class StackManagement {
    private static final Logger LOG = Logger.getLogger(StackManagement.class);

    private final Register GB = Register.GB;
    private final Register LB = Register.LB;
    private final Register SP = Register.SP;

    private LinkedList<Integer> idxAvailableGPRegisters;
    private LinkedList<Integer> idxUsedGPRegisters;

    private int offsetGB = 0;

    // Add counters to calculate "d"
    private int numSavedRegisters = 0;
    private int numVariables = 0;
    private int numTemporaries = 0;
    private int numMethodParams = 0;

    public StackManagement() {
        idxAvailableGPRegisters = new LinkedList<>();
        idxUsedGPRegisters = new LinkedList<>();

        for (int i = 2; i < Register.getMaxGPRegisters(); i++) { // R0 and R1 are scratch registers
            idxAvailableGPRegisters.add(i);
        }
    }

    public RegisterOffset addGlobalVariable() {
        offsetGB++;
        numVariables++;
        return new RegisterOffset(offsetGB, GB);
    }

    // Get the d number for TSTO instruction
    public int getNeededStackFrame() {
        return numSavedRegisters + numVariables + numTemporaries + numMethodParams; // 2 * numMethodParams because BSR makes 2 pushes
    }

    // Generate a comment explaining the stack size calculation
    public String getTSTOComment() {
        return numVariables + " (variables) + " + numSavedRegisters + " (saved registers) + " + numTemporaries + " (temporaries) + " + 2 * numMethodParams + " (method parameters x 2)";
    }

    private void saveRegister(DecacCompiler compiler, GPRegister reg) {
        LOG.debug("Saving register " + reg.toString());
        numSavedRegisters++;
        idxAvailableGPRegisters.add(idxUsedGPRegisters.removeFirst());
        compiler.addInstruction(new PUSH(reg), "Save register " + reg.toString());
    }

    public GPRegister getAvailableGPRegister(DecacCompiler compiler) throws RuntimeException {
        int idx = idxAvailableGPRegisters.removeFirst();
        idxUsedGPRegisters.addFirst(idx);
        GPRegister reg = Register.getR(idx);
        if (idxAvailableGPRegisters.isEmpty()) {
            saveRegister(compiler, reg); // Save the register
            idxAvailableGPRegisters.add(idx);
        }

        return reg;
    }

    public GPRegister getLastUsedRegister(DecacCompiler compiler) {
        return Register.getR(idxUsedGPRegisters.getFirst());
    }

    public void addAvailableGPRegister(GPRegister reg) {
        int idx = reg.getNumber();
        idxUsedGPRegisters.remove((Integer) idx);
        idxAvailableGPRegisters.add(idx);
    }

}
