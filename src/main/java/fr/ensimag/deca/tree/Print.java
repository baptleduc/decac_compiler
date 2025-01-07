package fr.ensimag.deca.tree;
import fr.ensimag.deca.tools.IndentPrintStream;
/**
 * @author gl12
 * @date 01/01/2025
 */
public class Print extends AbstractPrint {
    /**
     * @param arguments
     *            arguments passed to the print(...) statement.
     * @param printHex
     *            if true, then float should be displayed as hexadecimal (printx)
     */
    public Print(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    String getSuffix() {
        return "";
    }
}
