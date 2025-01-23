package fr.ensimag.arm;

import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.arm.instruction.ARMLabel;
import fr.ensimag.arm.instruction.AbstractARMInstruction;
import fr.ensimag.deca.context.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import org.apache.commons.lang.math.IntRange;

public class ARMProgram {

    static public final int INT_SIZE = 4;

    static public final int FLOAT_SIZE = 8;

    static public final HashMap<ARMDataType, String> dataSectionLines = new HashMap<ARMDataType, String>() {
        {
            put(ARMDataType.ASCIZ, ".asciz");
            put(ARMDataType.BYTE, ".byte");
            put(ARMDataType.WORD, ".word");
            put(ARMDataType.DOUBLE_WORD, ".double");
        }
    };

    static public final IntRange RANGE_ARG_REGISTER = new IntRange(0, 7);
    static public final IntRange RANGE_SCRATCH_REGISTERS = new IntRange(8, 15);

    static public final String FRAME_POINTER = "X29";

    static public final String LINK_REGISTER = "X30";

    static public final String STACK_POINTER = "sp";

    static public final String PROGRAM_COUNTER = "pc";

    static public final String ZERO_REGISTER = "XZR";

    static public final String WORD = ".word";

    static public final String BYTE = ".byte";

    static public final String DOUBLE_WORD = ".double";

    static public final String CALL_KERNEL = "svc #0x80";

    private boolean usingClang = false;

    private final Stack<String> scratchRegisters = new Stack<String>();

    private final Stack<String> registersTypeS = new Stack<String>();

    private final Stack<String> registersTypeD = new Stack<String>();

    private final LinkedList<String> stringLines = new LinkedList<String>();

    private final LinkedList<AbstractARMInstruction> mainInstructions = new LinkedList<AbstractARMInstruction>();

    private HashMap<String, Variable> varOccurences = new HashMap<String, Variable>();

    private HashMap<String, Integer> memoryMap = new HashMap<String, Integer>();

    private int occurencesCounter = 0;

    private int stringNameCounter = 0;

    private int printNbParameters = -1; // -1 if no printf call

    private int labelNameCounter = 0;

    public int getSizeOfVar(String varName) {
        return varOccurences.get(varName).getSize();
    }

    public void addInstruction(AbstractARMInstruction inst) {
        mainInstructions.add(inst);
    }

    public ARMProgram() {
        int count = RANGE_SCRATCH_REGISTERS.getMaximumInteger() - RANGE_SCRATCH_REGISTERS.getMinimumInteger();
        for (int i = RANGE_SCRATCH_REGISTERS.getMaximumInteger(); i >= RANGE_SCRATCH_REGISTERS
                .getMinimumInteger(); i--) {
            scratchRegisters.push("w" + i);
            registersTypeD.push("d" + count);
            registersTypeS.push("s" + count);
            count--;
        }
    }

    public void setProc(boolean isM2) {
        usingClang = isM2;
    }

    public boolean isUsingClang() {
        return usingClang;
    }

    private int computeVarMemory() { // return the max offset
        assert memoryMap.isEmpty();

        boolean useFramePointer = printNbParameters > -1; // && isUsingClang();

        int multiplier = useFramePointer ? -1 : 1;

        int offset = 0;

        for (String varName : varOccurences.keySet()) {
            for (String varName2 : memoryMap.keySet()) {
                if (varOccurences.get(varName).getFirstOccurence() > varOccurences.get(varName2).getSecondOccurence()) {
                    memoryMap.put(varName, memoryMap.get(varName2));
                    varOccurences.get(varName2).setSecondOccurence(varOccurences.get(varName).getSecondOccurence()); // work
                                                                                                                     // around
                                                                                                                     // to
                    // avoid offset
                    // conflict
                    break;
                }
            }
            if (!memoryMap.containsKey(varName)) {
                int newOffset = offset + varOccurences.get(varName).getSize() * multiplier;
                memoryMap.put(varName, multiplier == -1 ? newOffset : offset);
                offset = newOffset;
            }
        }

        return offset * multiplier; // the value is positive
    }

    public void addVarOccurence(String varName, int size) {
        if (varOccurences.containsKey(varName)) {
            varOccurences.get(varName).setSecondOccurence(occurencesCounter);
        } else {
            varOccurences.put(varName, new Variable(occurencesCounter, occurencesCounter, size));
        }
        occurencesCounter++;
    }

    public int getVarOffset(String varName) {
        return memoryMap.get(varName);
    }

    public void setPrintNbParametersIfSup(int nb) {
        printNbParameters = Math.max(printNbParameters, nb);
    }

    public String getAvailableRegister() {
        return scratchRegisters.pop();
    }

    public String getAvailableRegisterTypeD() {
        return registersTypeD.pop();
    }

    public String getAvailableRegisterTypeS() {
        return registersTypeS.pop();
    }

    public void freeRegister(String register) {
        if (register.contains("w")) {
            scratchRegisters.push(register);
        } else if (register.contains("d")) {
            registersTypeD.push(register);
        } else if (register.contains("s")) {
            registersTypeS.push(register);
        }
    }

    public void freeRegisterTypeD(String register) {
        registersTypeD.push(register);
    }

    public void freeRegisterTypeS(String register) {
        registersTypeS.push(register);
    }

    public void freeRegisterTypeW(String register) {
        scratchRegisters.push(register);
    }

    public String addStringLine(String value) {
        String name = "strl_" + stringNameCounter++;
        stringLines.add(name + ": .asciz \"" + value + "\"");
        return name;
    }

    public void addLabelLine(String label) {
        mainInstructions.add(new ARMLabel(label));
    }

    public String createLabel() {
        return "label_" + labelNameCounter++;
    }

    private int getNextPowerOf2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Input must be a positive integer.");
        }

        // If n is already a power of 2, return n
        if ((n & (n - 1)) == 0) {
            return n;
        }

        // Find the next power of 2
        int count = 0;
        while (n != 0) {
            n >>= 1;
            count += 1;
        }

        return 1 << count;
    }

    private LinkedList<String> genBeginningLines(int spSize) {
        boolean usingFP = printNbParameters > -1; // && isUsingClang();
        spSize = usingFP ? spSize + 16 : spSize;
        LinkedList<String> lines = new LinkedList<String>();
        lines.add(isUsingClang() ? ".globl _main" : ".globl main");
        lines.add(".p2align 2");
        lines.add(isUsingClang() ? "_main:" : "main:");
        lines.add(".cfi_startproc");
        lines.add("sub sp, sp, #" + (spSize));
        lines.add(".cfi_def_cfa_offset " + spSize);
        // lines.add("mov w0, #0"); // return 0
        // lines.add("str wzr, [sp, #" + (spSize-4) + "]"); // if no print call else x29
        // #-4
        if (usingFP) {
            lines.add("stp X29, X30, [sp, #" + (spSize - 16) + "]"); // save frame pointer and link register
            lines.add("add X29, sp, #" + (spSize - 16));
        }
        return lines;
    }

    private LinkedList<String> genEndingLines(int spSize) {
        boolean usingFP = printNbParameters > -1; // && isUsingClang();
        spSize = usingFP ? spSize + 16 : spSize;
        LinkedList<String> lines = new LinkedList<String>();
        if (usingFP) {
            lines.add("ldp X29, X30, [sp, #" + (spSize - 16) + "]"); // restore frame pointer and link register
        }
        lines.add("add sp, sp, #" + (spSize));
        lines.add("mov w0, #0"); // not sure
        lines.add("ret");
        lines.add(".cfi_endproc");
        // codeLines.add("mov X0, #0");
        // codeLines.add("bl _exit");
        return lines;
    }

    private int getSpSize(int spMaxOffset) {
        boolean usingFP = printNbParameters > -1; // && isUsingClang();
        if (!usingFP) {
            return Math.max(getNextPowerOf2(spMaxOffset), 16);
        }
        return Math.max(getNextPowerOf2(spMaxOffset + printNbParameters * 8), 16);
    }

    public LinkedList<String> genAssemblyCode() {
        LinkedList<String> codeLines = new LinkedList<String>();
        int spMaxOffset = computeVarMemory();
        int spSize = getSpSize(spMaxOffset);
        codeLines.addAll(genBeginningLines(spSize));

        for (AbstractARMInstruction inst : mainInstructions) {
            codeLines.add(inst.toString());
        }

        codeLines.addAll(genEndingLines(spSize));

        for (String line : stringLines) {
            codeLines.add(line);
        }

        return codeLines;
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

    public String getReadyRegister(ARMDVal dval) {
        String reg;
        if (dval.getForInstructionVal().contains("#")) {
            reg = getAvailableRegister();
            addInstruction(new ARMInstruction("mov", reg, dval.toString()));
        } else if (dval.isFloat()) {
            reg = getAvailableRegisterTypeD();
            String sreg = dval.toString();
            addInstruction(new ARMInstruction("fcvt", reg, sreg));
            freeRegisterTypeS(sreg);
        } else {
            reg = dval.toString();
        }
        return reg;
    }
}