package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodBody extends AbstractMethodBody {

    private ListDeclVar vars;
    private ListInst insts;

    public MethodBody(ListDeclVar vars, ListInst insts) {
        this.vars = vars;
        this.insts = insts;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" ... A FAIRE ... ");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
