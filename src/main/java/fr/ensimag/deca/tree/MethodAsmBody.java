package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodAsmBody extends AbstractMethodBody {

    private AbstractStringLiteral asmName;

    public MethodAsmBody(AbstractStringLiteral asmname) {
        asmName = asmname;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" ... A FAIRE ... ");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
