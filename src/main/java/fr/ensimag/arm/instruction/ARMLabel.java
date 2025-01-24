package fr.ensimag.arm.instruction;

public class ARMLabel extends AbstractARMInstruction {
    private final String label;

    public ARMLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label + ":";
    }

}
