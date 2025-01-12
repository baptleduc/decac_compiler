package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl12
 * @date 01/01/2025
 */
public class ListExpr extends TreeList<AbstractExpr> {

    @Override
    public void decompile(IndentPrintStream s) {
        boolean temp = true;
        for (AbstractExpr v : getList()) {
            if (temp) {
                temp = false;
                v.decompile(s);
            } else {
                s.print(",");
                v.decompile(s);
            }
        }
    }
}
