package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.log4j.Logger;

public class ErrorManager {
    private static final Logger LOG = Logger.getLogger(ErrorManager.class);

    // Label declaration for an IMA program
    public static final Label STACK_OVERFLOW_ERROR = new Label("stack_overflow_error");
    public static final Label IO_ERROR = new Label("io_error");
    public static final Label OVERFLOW_ERROR = new Label("overflow_error");

    public static Label getLabelStackOverflowError() {
        return STACK_OVERFLOW_ERROR;
    }

    public static Label getLabelIOError() {
        return IO_ERROR;
    }

    /**
     * Generates the assembly code for handling stack overflow errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    public static void generateStackOverflowError(DecacCompiler compiler) {
        generateError(STACK_OVERFLOW_ERROR, "Error: Stack Overflow", compiler);
    }

    /**
     * Generates the assembly code for handling I/O errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    public static void generateIOError(DecacCompiler compiler) {
        generateError(IO_ERROR, "Error: Input/Output error", compiler);
    }

    /**
     * Generates the assembly code for handling arithmetic overflow errors.
     *
     * @param compiler
     *            the Deca compiler instance
     */
    public static void generateOverflowError(DecacCompiler compiler) {
        generateError(OVERFLOW_ERROR, "Error: Overflow during arithmetic operation", compiler);
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

}
