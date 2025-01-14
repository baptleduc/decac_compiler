package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclField.class);

    // /**
    // * Pass 2 of [SyntaxeContextuelle]
    // */
    // void verifyDeclField(DecacCompiler compiler) throws ContextualError {
    // LOG.debug("verify listClass: start");
    // for(AbstractDeclClass declClass : getList()){
    // declClass.verifyClass(compiler);
    // }
    // // LOG.debug("verify listClass: end");
    // }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
}
