package fr.ensimag.arm.instruction;

import fr.ensimag.arm.ARMProgram;

public class ARMLoad extends ARMLoadStore {

    public ARMLoad(String register, String varName, ARMProgram program) {
        this.varName = varName;
        this.register = register;
        this.offset = program.getVarOffset(varName);
        this.instructionLabel = offset < 0 ? "stur" : "str";
    }
}
