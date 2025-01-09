package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.util.LinkedList;

import org.apache.log4j.Logger;

public class StackManagement {
    private static final Logger LOG = Logger.getLogger(StackManagement.class);

    private IMAProgram program;

    private final Register GB = Register.GB;
    private final Register LB = Register.LB;
    private final Register SP = Register.SP;

    private LinkedList<Integer> idxAvailableGPRegisters;
    private LinkedList<Integer> idxUsedGPRegisters;

    private int offsetGB = 1;
    private int offsetLB = 1;
    private int offsetSP = 1;

    // Add counters to calculate "d"
    private int numSavedRegisters = 0;
    private int numVariables = 0;
    private int numTemporaries = 0;
    private int numMethodParams = 0;

    public StackManagement(IMAProgram program, int numRegisters) {
        idxAvailableGPRegisters = new LinkedList<>();
        idxUsedGPRegisters = new LinkedList<>();
        this.program = program;

        int numberOfRegisters;
        if (numRegisters == -1) { // No limitations on the registers to be used
            numberOfRegisters = Register.getMaxGPRegisters();
        } else {
            numberOfRegisters = numRegisters;
        }
        for (int i = 2; i < numberOfRegisters; i++) { // R0 and R1 are scratch registers
            idxAvailableGPRegisters.add(i);
        }
    }

    public void incrementNumSavedRegisters() {
        numSavedRegisters++;
    }

    public String getCommentTSTO() {
        return numVariables + " (variables) + " + numSavedRegisters + " (saved registers) + " + numTemporaries
                + " (temporaries) + " + 2 * numMethodParams + " (method parameters x 2)";
    }

    public int getOffsetGB() {
        return offsetGB;
    }

    public int getOffsetLB() {
        return offsetLB;
    }

    public int getOffsetSP() {
        return offsetSP;
    }

    public RegisterOffset addGlobalVariable() {
        offsetGB++;
        numVariables++;
        return new RegisterOffset(offsetGB, GB);
    }

    public IMAProgram getProgram() {
        return program;
    }

    /**
     * Calculates the required stack frame size for the TSTO instruction.
     * Includes saved registers, variables, temporaries, and method parameters.
     *
     * @return the size of the stack frame needed for the TSTO instruction
     */
    public int getNeededStackFrame() {
        return numSavedRegisters + numVariables + numTemporaries + numMethodParams; // 2 * numMethodParams because BSR
                                                                                    // makes 2 pushes
    }

    /**
     * Retrieves the last used general-purpose register (GPRegister) from the list
     * of used registers index.
     *
     * @return the last used GPRegister
     */
    public GPRegister getLastUsedRegister() {
        return Register.getR(idxUsedGPRegisters.getFirst());
    }

    public GPRegister popUsedRegister() {
        int idx = idxUsedGPRegisters.removeFirst();
        return Register.getR(idx);
    }

    public GPRegister popAvailableGPRegister() {
        return Register.getR(idxAvailableGPRegisters.removeFirst());
    }

    public boolean isAvailableGPRegisterEmpty() {
        return idxAvailableGPRegisters.isEmpty();
    }

    /**
     * Marks the given general-purpose register (GPRegister) as available for future
     * use.
     *
     * @param reg
     *            the GPRegister to mark as available
     */
    public void pushAvailableGPRegister(GPRegister reg) {
        int idx = reg.getNumber();
        idxAvailableGPRegisters.addFirst(idx);
    }

    public void pushUsedGPRegister(GPRegister reg) {
        int idx = reg.getNumber();
        idxUsedGPRegisters.addFirst(idx);
    }

    public String debugAvailableRegister(){
        String res = "Stack of available registers: ";
        for (int i = 0; i < idxAvailableGPRegisters.size(); i++) {
            res += Register.getR(idxAvailableGPRegisters.get(i)).toString() + " ";
        }
        return res;
    }

    public String debugUsedRegister(){
        String res = "Stack of Used registers: ";
        for (int i = 0; i < idxUsedGPRegisters.size(); i++) {
            res += Register.getR(idxUsedGPRegisters.get(i)).toString() + " ";
        }
        return res;
    }

}
