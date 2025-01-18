package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.log4j.Logger;

public class ErrorManager {
    private static final Logger LOG = Logger.getLogger(ErrorManager.class);

    /**
     * Generates the assembly code for handling stack overflow errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateStackOverflowError(DecacCompiler compiler) {
        generateError(LabelManager.STACK_OVERFLOW_ERROR.getLabel(), "Error: Stack Overflow", compiler);
    }

    /**
     * Generates the assembly code for handling I/O errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateIOError(DecacCompiler compiler) {
        generateError(LabelManager.IO_ERROR.getLabel(), "Error: Input/Output error", compiler);
    }

    /**
     * Generates the assembly code for handling arithmetic overflow errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateOverflowError(DecacCompiler compiler) {
        generateError(LabelManager.OVERFLOW_ERROR.getLabel(), "Error: Overflow during arithmetic operation", compiler);
    }

    /**
     * Generates the assembly code for handling arithmetic overflow errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateCastError(DecacCompiler compiler) {
        generateError(LabelManager.CAST_ERROR.getLabel(), "Error: Cast invalid", compiler);
    }

    /**
     * Generates the assembly code for handling division by zero errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateDivideByZeroError(DecacCompiler compiler) {
        generateError(LabelManager.DIVIDE_BY_ZERO_ERROR.getLabel(), "Error: Division by zero", compiler);
    }

    /**
     * Generates the assembly code for handling division by zero errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateHeapOverflowError(DecacCompiler compiler) {
        generateError(LabelManager.HEAP_OVERFLOW_ERROR.getLabel(), "Error: Heap Overflow", compiler);
    }

    /**
     * Generates the assembly code for handling division by zero errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateNullPointerError(DecacCompiler compiler) {
        generateError(LabelManager.NULL_POINTER_ERROR.getLabel(), "Error: Deferencing a null pointer", compiler);
    }

    /**
     * Generates the assembly code for a specific error handler.
     *
     * @param label
     *            the label for the error handler
     * @param message
     *            the error message to display
     * @param compiler
     *            the Deca compiler instance
     */
    private static void generateError(Label label, String message, DecacCompiler compiler) {
        compiler.addLabel(label);
        compiler.addInstruction(new WSTR(message));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    private ErrorManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void generateAllErrors(DecacCompiler compiler) {
        generateStackOverflowError(compiler);
        generateOverflowError(compiler);
        generateIOError(compiler);
        generateDivideByZeroError(compiler);
        generateHeapOverflowError(compiler);
        generateNullPointerError(compiler);
        generateCastError(compiler);
    }

}
