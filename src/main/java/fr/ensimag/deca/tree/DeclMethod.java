package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclMethod extends AbstractDeclMethod {

    private AbstractIdentifier visibility;
    private AbstractIdentifier name;
    private ListDeclParam params;
    private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier visibility, AbstractIdentifier name, ListDeclParam params,
            AbstractMethodBody body) {
        this.visibility = visibility;
        this.name = name;
        this.params = params;
        this.body = body;
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
