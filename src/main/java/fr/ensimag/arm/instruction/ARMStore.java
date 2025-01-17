package fr.ensimag.arm.instruction;

import fr.ensimag.arm.ARMProgram;

public class ARMStore extends ARMLoadStore {

    public ARMStore(String register, String varName, ARMProgram program) {
        this.varName = varName;
        this.register = register;

        int offset = this.updateAssociatedProgramAndGetOffset(program);
        this.offset = offset;
        this.instructionLabel = offset < 0 ? "stur" : "str";
    }

    private int updateAssociatedProgramAndGetOffset(ARMProgram program) {
        // return the offset
        if (program.isVarInMemory(varName)) {
            return program.getVarOffset(varName);
        }
        return program.addVarToMemory(varName);
    }
    
}
