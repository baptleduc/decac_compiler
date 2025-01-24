package fr.ensimag.arm;

import fr.ensimag.arm.instruction.ARMInstruction;

public class ARMLoadedRegister {

    private String reg;

    private ARMDVal dval;

    private ARMProgram program;

    private boolean isFloat = false;

    private boolean isInt = false;

    public ARMLoadedRegister(ARMDVal value, ARMProgram prog){
        this.dval = value;
        this.program = prog;

        if (value.isImmediate() && value.isFloat()){
            genImmFloat();
        } else if (value.isImmediate()) {
            genImmInt();
        } else if (value.isFloat()) {
            genRegFloat();
        } else {
            genReg();
        }
    }

    public String getReg(){
        return reg;
    }

    public void freeReg(){
        assert reg.length() < 5;
        program.freeRegister(reg);
    }

    public boolean isFloat(){
        return isFloat;
    }

    public boolean isInt(){
        return isInt;
    }

    private void genImmFloat(){
        String reg = program.getAvailableRegister();
        String floatReg = program.getAvailableRegisterTypeS();

        int intBitsRepresentation = Float.floatToIntBits(dval.getValueFloat());
        int lower16 = intBitsRepresentation & 0xFFFF;
        int upper16 = (intBitsRepresentation >>> 16) & 0xFFFF;

        program.addInstruction(new ARMInstruction("mov", reg, lower16));
        program.addInstruction(new ARMInstruction("movk", reg, "#" + upper16, "lsl #16"));
        program.addInstruction(new ARMInstruction("fmov", floatReg, reg));

        program.freeRegister(reg);

        this.reg = floatReg;
        this.isFloat = true;
    }

    private void genImmInt(){
        String reg = program.getAvailableRegister();
        program.addInstruction(new ARMInstruction("mov", reg, dval.getValueInt()));
        this.reg = reg;
        this.isInt = true;
    }

    private void genRegFloat(){
        if (dval.toString().contains("d")){
            String reg = program.getAvailableRegisterTypeS();
            program.addInstruction(new ARMInstruction("fcvt", reg, dval.toString()));
            program.freeRegister(dval.toString());
            this.reg = reg;
            return;
        }
        this.reg = dval.toString();
        this.isFloat = true;
    }

    private void genReg(){
        this.reg = dval.toString();
        this.isInt = true;
    }

    @Override
    public String toString() {
        return reg;
    }

}
    

