package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
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

    private int offsetGB = 0;
    private int offsetLB = 0;
    private int offsetSP = 0;

    // Add counters to calculate "d"
    private int numSavedRegisters = 0;
    private int numVariables = 0;
    private int numTemporaries = 0;
    private int numMethodParams = 0;

    public StackManagement(IMAProgram program) {
        idxAvailableGPRegisters = new LinkedList<>();
        idxUsedGPRegisters = new LinkedList<>();
        this.program = program;

        for (int i = 2; i < Register.getMaxGPRegisters(); i++) { // R0 and R1 are scratch registers
            idxAvailableGPRegisters.add(i);
        }
    }

    public RegisterOffset addGlobalVariable() {
        offsetGB++;
        numVariables++;
        return new RegisterOffset(offsetGB, GB);
    }

    public void withProgram(IMAProgram program, Runnable r) {
        IMAProgram oldProgram = this.program;
        this.program = program;
        r.run();
        this.program = oldProgram;
    }

    public IMAProgram getProgram() {
        return program;
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addFirst(fr.ensimag.ima.pseudocode.Instruction,,java.lang.String)
     */
    public void addFirst(Instruction i, String comment) {
        program.addFirst(i, comment);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     *      java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    /**
     * Inserts a TSTO instruction to test for stack overflow and calculates
     * the required stack size. If 'noVerify' is false, it adds a block to handle
     * stack overflow errors.
     *
     * @param noVerify if true, skips adding the stack overflow error-handling block
     */
    public void stackOverflowCheck(boolean noVerify) {
        if (noVerify){
            return;
        }
        LOG.debug("Inserting TSTO instruction");
        LOG.debug(noVerify);
        int d = getNeededStackFrame();
        Label label = new Label("stack_overflow_error");
        ImmediateInteger imm = new ImmediateInteger(++offsetSP);
        program.addFirst(new ADDSP(imm));
        program.addFirst(new BOV(label));
        program.addFirst(new TSTO(d), numVariables + " (variables) + " + numSavedRegisters + " (saved registers) + " + numTemporaries + " (temporaries) + " + 2 * numMethodParams + " (method parameters x 2)");
        program.addLabel(label);
        program.addInstruction(new WSTR("Error: Stack Overflow"));
        program.addInstruction(new WNL());
        program.addInstruction(new ERROR());
    }

    /**
     * Adds a LOAD instruction to load an immediate value into an available register.
     *
     * @param value the immediate value to be loaded into the register
     */
    public void loadImmediateValue(int value) {
        GPRegister gpReg = getAvailableGPRegister();
        program.addInstruction(new LOAD(value, gpReg));
    }

    /**
     * Stores the value of the last used register into the specified memory address
     * and releases the register for future use.
     *
     * @param addr the memory address where the last used register's value will be stored
     */
    public void storeLastUsedRegister(DAddr addr) {
        GPRegister lastUsedRegister = getLastUsedRegister();
        program.addInstruction(new STORE(lastUsedRegister, addr));
        // Free the register because its value is stored in memory
        addAvailableGPRegister(lastUsedRegister);
    }

    /**
     * Adds a labeled error-handling block for stack overflow to the program.
     * This block outputs an error message and terminates the program.
     */
    private void insertStackOverflowErrorBlock(){
        program.addLabel(new Label("stack_overflow_error"));
        program.addInstruction(new WSTR("Error: Stack Overflow"));
        program.addInstruction(new WNL());
        program.addInstruction(new ERROR());
    }

    /**
     * Calculates the required stack frame size for the TSTO instruction.
     * Includes saved registers, variables, temporaries, and method parameters.
     *
     * @return the size of the stack frame needed for the TSTO instruction
     */
    private int getNeededStackFrame() {
        return numSavedRegisters + numVariables + numTemporaries + numMethodParams; // 2 * numMethodParams because BSR makes 2 pushes
    }

    /**
     * Saves the given register onto the stack by pushing it and marks it as available for reuse.
     * Updates the list of available and used registers index.
     *
     * @param reg the register to be saved onto the stack
     */
    private void saveRegister(GPRegister reg) {
        LOG.debug("Saving register " + reg.toString());
        numSavedRegisters++;
        idxAvailableGPRegisters.add(idxUsedGPRegisters.removeFirst());
        program.addInstruction(new PUSH(reg), "Save register " + reg.toString());
    }

    /**
     * Retrieves the next available general-purpose register (GPRegister).
     * If no registers are available, it saves the currently used register onto the stack
     * to free up space and makes it available for reuse.
     *
     * @return the next available GPRegister
     */
    public GPRegister getAvailableGPRegister(){
        int idx = idxAvailableGPRegisters.removeFirst();
        idxUsedGPRegisters.addFirst(idx);
        GPRegister reg = Register.getR(idx);
        if (idxAvailableGPRegisters.isEmpty()) {
            saveRegister(reg); // Save the register
            idxAvailableGPRegisters.add(idx);
        }

        return reg;
    }

    /**
     * Retrieves the last used general-purpose register (GPRegister) from the list of used registers index.
     *
     * @return the last used GPRegister
     */
    public GPRegister getLastUsedRegister() {
        return Register.getR(idxUsedGPRegisters.getFirst());
    }

    /**
     * Marks the given general-purpose register (GPRegister) as available for future use.
     * Updates the list of used and available registers index.
     *
     * @param reg the GPRegister to mark as available
     */
    public void addAvailableGPRegister(GPRegister reg) {
        int idx = reg.getNumber();
        idxUsedGPRegisters.remove((Integer) idx);
        idxAvailableGPRegisters.add(idx);
    }

}
