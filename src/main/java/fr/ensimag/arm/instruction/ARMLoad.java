package fr.ensimag.arm.instruction;

import fr.ensimag.arm.ARMProgram;

public class ARMLoad extends ARMLoadStore {

    public ARMLoad(String register, String varName, ARMProgram program) {
        super(register, varName, program);
    }

    @Override
    protected String getInstructionLabel(int offset) {
        return offset < 0 ? "ldur" : "ldr";
    }
}
