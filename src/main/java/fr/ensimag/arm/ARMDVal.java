package fr.ensimag.arm;

public class ARMDVal {

    protected String forInstructionVal;

    protected String trueVal;

    protected String varName;

    protected int valueInt;

    protected float valueFloat;

    private boolean isFloat = false;

    private boolean isImm = false;

    public ARMDVal(String val) {
        if (val.contains("s")) {
            isFloat = true;
        }
        this.forInstructionVal = val;
        this.trueVal = val;
    }

    public ARMDVal(int val) {
        this.forInstructionVal = "#" + val;
        this.trueVal = "" + val;
        this.valueInt = val;
        this.isImm = true;
    }

    public ARMDVal(float val) {
        this.forInstructionVal = "#" + val;
        this.trueVal = "" + val;
        this.valueFloat = val;
        this.isImm = true;
    }

    public ARMDVal(String val, String name) {
        if (val.contains("s")) {
            isFloat = true;
        }
        this.forInstructionVal = val;
        this.trueVal = val;
        this.varName = name;
    }

    public boolean isImmediate() {
        return isImm;
    }

    public int getValueInt() {
        return valueInt;
    }

    public float getValueFloat() {
        return valueFloat;
    }

    public String getForInstructionVal() {
        return forInstructionVal;
    }

    public String getTrueVal() {
        return trueVal;
    }

    public String getVarName() {
        return varName;
    }

    public boolean isFloat() {
        return isFloat;
    }

    @Override
    public String toString() {
        return forInstructionVal;
    }

}
