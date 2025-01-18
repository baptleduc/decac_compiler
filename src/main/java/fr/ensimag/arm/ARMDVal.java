package fr.ensimag.arm;

public class ARMDVal {

    String forInstructionVal;

    String trueVal;

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

    public int getValueInt() {
        return valueInt;
    }

    public String getForInstructionVal() {
        return forInstructionVal;
    }

    public String getTrueVal() {
        return trueVal;
    }

    @Override
    public String toString() {
        return forInstructionVal;
    }
    
}
