package fr.ensimag.arm.instruction;

public abstract class AbstractARMInstruction {
 
    protected String instructionLabel;

    protected String p1 = null;

    protected String p2 = null;

    protected String p3 = null;

    public String getInstructionLabel() {
        return instructionLabel;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getP3() {
        return p3;
    }

    @Override
    public String toString() {
        String res = instructionLabel;
        if (p1 != null) {
            res += " " + p1;
        }
        if (p2 != null) {
            res += ", " + p2;
        }
        if (p3 != null) {
            res += ", " + p3;
        }
        return res;
    }
}
