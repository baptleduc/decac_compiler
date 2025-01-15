package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodBody extends AbstractMethodBody {

    private ListDeclVar listDeclVar;
    private ListInst listInst;

    public MethodBody(ListDeclVar listDeclVar, ListInst listInst) {
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        listDeclVar.decompile(s);
        listInst.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        listDeclVar.iter(f);
        listInst.iter(f);
    }

}
