package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclField extends AbstractDeclField {

    private AbstractIdentifier visibility;
    private AbstractIdentifier name;
    private AbstractInitialization init;

    public DeclField(AbstractIdentifier visibility, AbstractIdentifier name, AbstractInitialization init) {
        this.visibility = visibility;
        this.name = name;
        this.init = init;
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
