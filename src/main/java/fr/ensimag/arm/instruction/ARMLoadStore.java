package fr.ensimag.arm.instruction;

public abstract class ARMLoadStore extends AbstractARMInstruction {

    protected String varName;

    protected String register;

    protected int offset;

    @Override
    public String toString() {
        if (offset != 0) {
            return instructionLabel + " " + register + ", [" + varName + ", #" + offset + "]";
        }
        return instructionLabel + " " + register + ", [" + varName + "]";
    }
}
