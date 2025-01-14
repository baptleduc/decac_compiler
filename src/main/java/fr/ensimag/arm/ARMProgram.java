package fr.ensimag.arm;

import java.util.LinkedList;
import java.util.Stack;

import fr.ensimag.deca.context.Type;

public class ARMProgram {
    static public final int MAX_REGISTERS = 16;

    static public final String WORD = ".word";

    static public final String BYTE = ".byte";

    static public final String DOUBLE_WORD = ".double";

    private final Stack<String> registerStack = new Stack<String>();

    private final LinkedList<String> dataSectionLines = new LinkedList<String>();

    private final LinkedList<String> bssSectionLines = new LinkedList<String>();

    private final LinkedList<String> textSectionLines = new LinkedList<String>();

    private final LinkedList<String> bodyLines = new LinkedList<String>();

    public ARMProgram() {
        for (int i = MAX_REGISTERS-1; i >= 0; i--) {
            registerStack.push("r" + i);
        }
    }

    public String getAvailableRegister(){
        return registerStack.pop();
    }

    public void freeRegister(String register){
        registerStack.push(register);
    }

    static public int getSizeForType(String type) {
        switch (type) {
            case BYTE:
                return 1;
            case WORD:
                return 4;
            case DOUBLE_WORD:
                return 8;
            default:
                return 0;
        }
    }

    public void addDataSectionLine(String varType, String varName, String value) {
        assert varType.equals(WORD) || varType.equals(BYTE) || varType.equals(DOUBLE_WORD);
        dataSectionLines.add(varName + ": " + varType + " " + value);
    }

    public void addBssSectionLine(String varName, int size) {
        bssSectionLines.add( ".comm "+ varName + ", " + size);
    }

    public void addTextSectionLine(String line) {
        textSectionLines.add(line);
    }

    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    public void addInstructionARM(String instruction, String p1) {
        addBodyLine(instruction + " " + p1);
    }

    public void addInstructionARM(String instruction, String p1, String p2) {
        addBodyLine(instruction + " " + p1 + ", " + p2);
    }

    public void addInstructionARM(String instruction, String p1, String p2, String p3) {
        addBodyLine(instruction + " " + p1 + ", " + p2 + ", " + p3);
    }

    public void addInstructionSetMem(String varName, String reg) {
        String regLdr = getAvailableRegister();
        addBodyLine("ldr " + regLdr + ", =" + varName);
        addBodyLine("str " + reg + ", [" + regLdr + "]");
        freeRegister(regLdr);
    }

    public void display() {
        System.out.println(".section .data");
        for (String line : dataSectionLines) {
            System.out.println(line);
        }

        System.out.println(".section .bss");
        for (String line : bssSectionLines) {
            System.out.println(line);
        }

        System.out.println(".section .text");
        for (String line : textSectionLines) {
            System.out.println(line);
        }

        for (String line : bodyLines) {
            System.out.println(line);
        }
    }

    public static String getARMTypeFromDecaType(Type type) {
        if (type.isInt()) {
            return WORD;
        } else if (type.isFloat()) {
            return DOUBLE_WORD;
        } else {
            return BYTE;
        }
    }
}