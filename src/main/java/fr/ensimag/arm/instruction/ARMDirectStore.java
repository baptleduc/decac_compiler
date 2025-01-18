package fr.ensimag.arm.instruction;

public class ARMDirectStore extends AbstractARMInstruction {

    private String register;
    private int offset;

    public ARMDirectStore(String register, int offset) {
        this.instructionLabel = "str";
        this.register = register;
        this.offset = offset;
    }    

    @Override
    public String toString() {
        if (offset > 0){
            return instructionLabel + " " + register + ", [sp, #" + offset + "]";
        }
        return instructionLabel + " " + register + ", [sp]";
    }
}
