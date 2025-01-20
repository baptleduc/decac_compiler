package fr.ensimag.arm;

import fr.ensimag.arm.instruction.AbstractARMInstruction;
import fr.ensimag.deca.context.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import org.apache.commons.lang.math.IntRange;

public class ARMProgram {

    static public final HashMap<ARMDataType, String> dataSectionLines = new HashMap<ARMDataType, String>() {
        {
            put(ARMDataType.ASCIZ, ".asciz");
            put(ARMDataType.BYTE, ".byte");
            put(ARMDataType.WORD, ".word");
            put(ARMDataType.DOUBLE_WORD, ".double");
        }
    };

    static public final IntRange RANGE_ARG_REGISTER = new IntRange(0, 7);
    static public final IntRange RANGE_SCRATCH_REGISTERS = new IntRange(9, 15);


    static public final String FRAME_POINTER = "X29";

    static public final String LINK_REGISTER = "X30";

    static public final String STACK_POINTER = "sp";

    static public final String PROGRAM_COUNTER = "pc";

    static public final String ZERO_REGISTER = "XZR";

    static public final String WORD = ".word";

    static public final String BYTE = ".byte";

    static public final String DOUBLE_WORD = ".double";

    static public final String CALL_KERNEL = "svc #0x80";

    private final Stack<String> scratchRegisters = new Stack<String>();

    private final LinkedList<String> stringLines = new LinkedList<String>();

    private final LinkedList<AbstractARMInstruction> mainInstructions = new LinkedList<AbstractARMInstruction>();

    private HashMap<String, IntTuple> varOccurences = new HashMap<String, IntTuple>();

    private HashMap<String, Integer> memoryMap = new HashMap<String, Integer>();

    private int occurencesCounter = 0;

    private int stringNameCounter = 0;

    private int printNbParameters = -1; // -1 if no printf call

    public void addInstruction(AbstractARMInstruction inst) {
        mainInstructions.add(inst);
    }

    public ARMProgram() {
        for (int i = RANGE_SCRATCH_REGISTERS.getMaximumInteger(); i >= RANGE_SCRATCH_REGISTERS
                .getMinimumInteger(); i--) {
            scratchRegisters.push("w" + i);
        }
    }

    // ############

    // public boolean isVarInMemory(String varName) {
    // return memoryMap.containsKey(varName);
    // }

    // public int addVarToMemory(String varName) {
    // // should only be called by the constructor of ARMStore
    // // return the offset of the new variable
    // memoryMap.put(varName, actualSpOffset);
    // int res = actualSpOffset;
    // actualSpOffset += 4;
    // return res;
    // }

    private int computeVarMemory() { // return the max offset, can be optimized, TODO ARM : different size
        assert memoryMap.isEmpty();
        int multiplier = printNbParameters == -1 ? 1 : -1; // the offset is negative (we use the frame pointer to store
                                                           // variables) if there is a printf call
        int offset = printNbParameters == -1 ? 0 : -4;

        for (String varName : varOccurences.keySet()) {
            for (String varName2 : memoryMap.keySet()) {
                if (varOccurences.get(varName).getFirst() > varOccurences.get(varName2).getSecond()) {
                    memoryMap.put(varName, memoryMap.get(varName2));
                    varOccurences.get(varName2).setSecond(varOccurences.get(varName).getSecond()); // work around to
                                                                                                   // avoid offset
                                                                                                   // conflict
                    break;
                }
            }
            if (!memoryMap.containsKey(varName)) {
                memoryMap.put(varName, offset);
                offset += 4 * multiplier; // only one size for now
            }
        }

        return offset * multiplier; // the value is positive
    }

    public void addVarOccurence(String varName) {
        if (varOccurences.containsKey(varName)) {
            varOccurences.get(varName).setSecond(occurencesCounter);
        } else {
            varOccurences.put(varName, new IntTuple(occurencesCounter, occurencesCounter));
        }
        occurencesCounter++;
    }

    // ###############

    public int getVarOffset(String varName) {
        return memoryMap.get(varName);
    }

    public void setPrintNbParametersIfSup(int nb) {
        printNbParameters = Math.max(printNbParameters, nb);
    }

    public String getAvailableRegister() {
        return scratchRegisters.pop();
    }

    public void freeRegister(String register) {
        scratchRegisters.push(register);
    }

    // static public int getSizeForType(String type) {
    // switch (type) {
    // case BYTE:
    // return 1;
    // case WORD:
    // return 4;
    // case DOUBLE_WORD:
    // return 8;
    // default:
    // return 0;
    // }
    // }

    public String addStringLine(String value) {
        String name = "strl_" + stringNameCounter++;
        stringLines.add(name + ": .asciz \"" + value + "\"");
        return name;
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
        LinkedList<String> lines = new LinkedList<String>();
        lines.add(".globl _main");
        lines.add(".p2align 2");
        lines.add("_main:");
        lines.add("sub sp, sp, #" + (spSize + 16));
        lines.add("stp X29, X30, [sp, #" + spSize + "]"); // save frame pointer and link register
        lines.add("add X29, sp, #" + spSize);
        return lines;
    }

    private LinkedList<String> genEndingLines(int spSize) {
        LinkedList<String> lines = new LinkedList<String>();
        lines.add("ldp X29, X30, [sp, #" + spSize + "]"); // restore frame pointer and link register
        lines.add("add sp, sp, #" + (spSize + 16));
        lines.add("ret");
        // codeLines.add("mov X0, #0");
        // codeLines.add("bl _exit");
        return lines;
    }

    private int getSpSize(int spMaxOffset) {
        int size = getNextPowerOf2(16 + spMaxOffset + printNbParameters * 8);
        return size;
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

    public void display() {
        for (String line : genAssemblyCode()) {
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