package fr.ensimag.arm.instruction;

import fr.ensimag.arm.ARMProgram;

public class ARMLoad extends ARMLoadStore {

    public ARMLoad(String register, String varName, ARMProgram program) {
        super(register, varName, program, ARMProgram.INT_SIZE);
    }

    public ARMLoad(String register, String varName, ARMProgram program, int size) {
        super(register, varName, program, size);
    }

    @Override
    protected String getInstructionLabel(int offset) {
        return offset < 0 ? "ldur" : "ldr";
    }
}
