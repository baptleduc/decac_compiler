package fr.ensimag.arm.instruction;

import fr.ensimag.arm.ARMProgram;

public abstract class ARMLoadStore extends AbstractARMInstruction {

    protected String varName;

    protected String register;

    protected ARMProgram program;

    protected ARMLoadStore(String register, String varName, ARMProgram program) {
        this.varName = varName;
        this.register = register;
        this.program = program;
        program.addVarOccurence(varName);
    }

    protected abstract String getInstructionLabel(int offset);

    @Override
    public String toString() {
        int offset = program.getVarOffset(varName);
        if (offset < 0) {
            return getInstructionLabel(offset) + " " + register + ", [X29, #" + offset + "]";
        }
        if (offset > 0) {
            return getInstructionLabel(offset) + " " + register + ", [sp, #" + offset + "]";
        }
        return getInstructionLabel(offset) + " " + register + ", [sp]";
    }
}
