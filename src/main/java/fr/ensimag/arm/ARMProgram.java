package fr.ensimag.arm;

import java.util.LinkedList;

import fr.ensimag.deca.context.Type;

public class ARMProgram {
    static public final String WORD = ".word";

    static public final String BYTE = ".byte";

    static public final String DOUBLE_WORD = ".double";

    private final LinkedList<String> dataSectionLines = new LinkedList<String>();

    private final LinkedList<String> bssSectionLines = new LinkedList<String>();

    private final LinkedList<String> textSectionLines = new LinkedList<String>();

    private final LinkedList<String> bodyLines = new LinkedList<String>();

    private boolean isComplexExpr = false; // workaround to know if a variable must be declared in the data or bss section

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

    public boolean isComplexExpr() {
        return isComplexExpr;
    }

    public void setComplexExpr(boolean complexExpr) {
        isComplexExpr = complexExpr;
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