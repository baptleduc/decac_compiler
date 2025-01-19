package fr.ensimag.arm;

public class ARMDVal {

    String forInstructionVal;

    String trueVal;

    String varName;

    int valueInt;

    public ARMDVal(String val) {
        this.forInstructionVal = val;
        this.trueVal = val;
    }

    public ARMDVal(int val) {
        this.forInstructionVal = "#" + val;
        this.trueVal = "" + val;
        this.valueInt = val;
    }

    public ARMDVal(String val, String name) {
        this.forInstructionVal = val;
        this.trueVal = val;
        this.varName = name;
    }

    public int getValueInt() {
        return valueInt;
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

    @Override
    public String toString() {
        return forInstructionVal;
    }
    
}
