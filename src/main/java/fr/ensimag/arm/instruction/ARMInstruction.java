
package fr.ensimag.arm.instruction;

public class ARMInstruction extends AbstractARMInstruction {

    public ARMInstruction(String instructionLabel) {
        this.instructionLabel = instructionLabel;
    }

    public ARMInstruction(String instructionLabel, String p1) {
        this.instructionLabel = instructionLabel;
        this.p1 = p1;
    }

    public ARMInstruction(String instructionLabel, int p1) {
        this.instructionLabel = instructionLabel;
        this.p1 = "#" + p1;
    }

    public ARMInstruction(String instructionLabel, String p1, String p2) {
        assert (!(instructionLabel == "mov" && p1.contains("w") && p2.contains("s")));
        this.instructionLabel = instructionLabel;
        this.p1 = p1;
        this.p2 = p2;
    }

    public ARMInstruction(String instructionLabel, String p1, int p2) {
        this.instructionLabel = instructionLabel;
        this.p1 = p1;
        this.p2 = "#" + p2;
    }

    public ARMInstruction(String instructionLabel, String p1, String p2, String p3) {
        this.instructionLabel = instructionLabel;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public ARMInstruction(String instructionLabel, String p1, String p2, int p3) {
        this.instructionLabel = instructionLabel;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = "#" + p3;
    }

}
