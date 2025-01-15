package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.MethodTable;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClass(compiler);
        }
        // LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler, AbstractIdentifier superClass,
            AbstractIdentifier nameClass) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClassMembers(compiler);
        }
        // LOG.debug("verify listClass: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListDeclClass(DecacCompiler compiler) {
        if (getList().isEmpty()) {
            return;
        }
        MethodTable.codeGenTableObjectClass(compiler);
        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenDeclClass(compiler);
        }
    }

}
