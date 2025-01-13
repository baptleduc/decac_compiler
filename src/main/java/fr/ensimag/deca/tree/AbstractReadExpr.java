package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

/**
 * read...() statement.
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    protected abstract void codeGenRead(DecacCompiler compiler);

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenRead(compiler);
        setDVal(compiler.getRegister1());
    }

}
