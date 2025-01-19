/**
 * Manages the stack and registers for the IMA program.
 * Provides methods to handle offsets, register allocation, and stack frame size calculation.
 * 
 * @param program the IMA program to manage
 * @param numRegisters the number of general-purpose registers available
 */
package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import java.util.LinkedList;
import org.apache.log4j.Logger;

public class StackManager {
    private static final Logger LOG = Logger.getLogger(StackManager.class);

    private IMAProgram program;

    private final Register GB = Register.GB;
    private final Register LB = Register.LB;
    private final Register SP = Register.SP;

    private LinkedList<Integer> idxAvailableGPRegisters;
    private LinkedList<Integer> idxUsedGPRegisters;

    private LinkedList<Integer> usedRegistersMethod;

    private int offsetGB = 0;
    private int offsetLB = 0;
    private int offsetSP = 0;

    private DAddr lastMethodTableAddr;

    // Add counters to calculate "d"
    private int numSavedRegisters = 0;
    private int numTemporaries = 0;
    private int numMethodParams = 0;

    public StackManager(IMAProgram program, int numRegisters) {
        idxAvailableGPRegisters = new LinkedList<>();
        idxUsedGPRegisters = new LinkedList<>();
        usedRegistersMethod = new LinkedList<>();
        this.program = program;

        for (int i = 2; i < numRegisters; i++) { // R0 and R1 are scratch registers
            idxAvailableGPRegisters.add(i);
        }
    }

    public void initStackForMethod() {
        offsetLB = 0;
        assert (usedRegistersMethod.isEmpty());
    }

    public void markRegisterUsedMethod(GPRegister reg) {
        usedRegistersMethod.addFirst(reg.getNumber());
    }

    public LinkedList<Integer> getUsedRegistersMethod() {
        return usedRegistersMethod;
    }

    public int popUsedRegisterMethod() {
        return usedRegistersMethod.removeFirst();
    }

    /**
     * Returns the GB register.
     */
    public Register getGBRegister() {
        return GB;
    }

    /**
     * Returns the register 1.
     */
    public GPRegister getRegister1() {
        return Register.getR(1);
    }

    /**
     * Returns the register 0.
     */
    public GPRegister getRegister0() {
        return Register.getR(0);
    }

    /**
     * Returns the LB register.
     */
    public Register getLBRegister() {
        return LB;
    }

    /**
     * Returns the SP register.
     */
    public Register getSPRegister() {
        return SP;
    }

    /**
     * Returns the current offset in the GB register.
     */
    public RegisterOffset getOffsetGB() {
        return new RegisterOffset(offsetGB, GB);
    }

    /**
     * Returns the current value of the GB offset.
     */
    public int getOffsetGBValue() {
        return offsetGB;
    }

    /**
     * Increments the GB offset by the specified value.
     */
    public void incrementOffsetGB(int value) {
        offsetGB += value;
    }

    /**
     * Increments the GB offset by 1.
     */
    public void incrementOffsetGB() {

        offsetGB++;
        LOG.debug("Incrementing offsetGB to " + offsetGB);
    }

    /**
     * Increments the number of saved registers by 1.
     */
    public void incrementNumSavedRegisters() {
        numSavedRegisters++;
    }

    /**
     * Returns the address of the last method table.
     */
    public DAddr getLastMethodTableAddr() {
        return lastMethodTableAddr;
    }

    /**
     * Sets the address of the last method table.
     */
    public void setLastMethodTableAddr(DAddr addr) {
        lastMethodTableAddr = addr;
    }

    public void incrementLastMethodTableAddr(int value) {
        lastMethodTableAddr = new RegisterOffset(getOffsetGBValue() + value, GB);
    }

    /**
     * Returns a comment string for the TSTO instruction.
     */
    public String getCommentTSTO() {
        return (offsetGB + offsetLB) + " (variables) + " + numSavedRegisters + " (saved registers) + " + numTemporaries
                + " (temporaries) + " + 2 * numMethodParams + " (method parameters x 2)";
    }

    /**
     * Returns the current offset in the LB register.
     */
    public int getOffsetLB() {
        return offsetLB;
    }

    /**
     * Returns the current offset in the SP register.
     */
    public int getOffsetSP() {
        return offsetSP;
    }

    /**
     * Adds a global variable and returns its offset in the GB register.
     */
    public RegisterOffset addGlobalVariable() {
        LOG.debug("Adding global variable at offset " + offsetGB);
        // TODO: switch case to determine the size of the offset and add type in arg
        return new RegisterOffset(++offsetGB, GB);
    }

    public RegisterOffset addLocalVariable() {
        LOG.debug("Adding local variable at offset " + offsetLB);
        return new RegisterOffset(++offsetLB, LB);
    }

    /**
     * Returns the current IMA program.
     */
    public IMAProgram getProgram() {
        return program;
    }

    /**
     * Calculates the required stack frame size for the TSTO instruction.
     * Includes saved registers, variables, temporaries, and method parameters.
     *
     * @return the size of the stack frame needed for the TSTO instruction
     */
    public int calculateTSTOSize() {
        return offsetGB + offsetLB + numSavedRegisters + numTemporaries + 2 * numMethodParams;
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

    /**
     * Pops the last used general-purpose register (GPRegister) from the list of
     * used
     * registers index.
     *
     * @return the last used GPRegister
     */
    public GPRegister popUsedRegister() {
        int idx = idxUsedGPRegisters.removeFirst();
        return Register.getR(idx);
    }

    /**
     * Pushes the given general-purpose register (GPRegister) to the list of used
     * registers index.
     *
     * @param reg
     *            the GPRegister to push
     */
    public GPRegister popAvailableGPRegister() {
        return Register.getR(idxAvailableGPRegisters.removeFirst());
    }

    /**
     * Retrieves the last available general-purpose register (GPRegister) from the
     * list of available registers index.
     *
     * @return the last available GPRegister
     */
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

    /**
     * Marks the given general-purpose register (GPRegister) as used.
     *
     * @param reg
     *            the GPRegister to mark as used
     */
    public void pushUsedGPRegister(GPRegister reg) {
        int idx = reg.getNumber();
        idxUsedGPRegisters.addFirst(idx);
    }

    /**
     * Used to debug the available registers.
     */
    public String debugAvailableRegister() {
        String res = "Stack of available registers: ";
        for (int i = 0; i < idxAvailableGPRegisters.size(); i++) {
            res += Register.getR(idxAvailableGPRegisters.get(i)).toString() + " ";
        }
        return res;
    }

    /**
     * Used to debug the used registers.
     */
    public String debugUsedRegister() {
        String res = "Stack of Used registers: ";
        for (int i = 0; i < idxUsedGPRegisters.size(); i++) {
            res += Register.getR(idxUsedGPRegisters.get(i)).toString() + " ";
        }
        return res;
    }

}
