package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import java.util.LinkedList;

public class StackManagement {
    private int stackSizeTracker = 0;
    private final Register GB = Register.GB;
    private final Register LB = Register.LB;
    private final Register SP = Register.SP;
    
    private LinkedList<Integer> idxAvailableGPRegisters;
    private LinkedList<Integer> idxUsedGPRegisters;
    private boolean isTmpRegister = false;

    private int offsetGB = 0;

    public StackManagement() {
        idxAvailableGPRegisters = new LinkedList<>();

        
        for (int i = 2; i < Register.getMaxGPRegisters(); i++) { // R0 and R1 are scratch registers
            idxAvailableGPRegisters.add(i);
        }
    }
    public int getStackSizeTracker() {
        return stackSizeTracker;
    }

    public void incrementMaxStack(int increment) {
        stackSizeTracker += increment;
    }

    public void incrementMaxStack() {
        incrementMaxStack(1);
    }

    public RegisterOffset addGlobalVariable(){
        offsetGB++;
        stackSizeTracker++;
        return new RegisterOffset(offsetGB, GB);
    }
    // private GPRegister restoreTmpRegister(DecacCompiler compiler) {
    //     if (isTmpRegister) {
    //         compiler.addInstruction(new POP(Register.getR(0)), "Temporary register deallocation");
    //         idxUsedGPRegisters.removeLast();
    //         idxAvailableGPRegisters.add(idxUsedGPRegisters.removeLast());
    //         isTmpRegister = false;
    //     }
    // }
    private void allocateTmpRegister(DecacCompiler compiler, GPRegister reg) {
        isTmpRegister = true;
        idxAvailableGPRegisters.add(idxUsedGPRegisters.removeFirst());
        compiler.addInstruction(new PUSH(reg), "Temporary register allocation");
    }
    public GPRegister getAvailableGPRegister(DecacCompiler compiler) throws RuntimeException {
        int idx = idxAvailableGPRegisters.removeFirst();
        idxUsedGPRegisters.addFirst(idx);
        GPRegister reg = Register.getR(idx);
        if (idxAvailableGPRegisters.isEmpty()) {
            allocateTmpRegister(compiler, reg); // Save the register
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
