package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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
        compiler.addInstruction(new BOV(LabelManager.IO_ERROR.getLabel()));
        setDVal(compiler.getRegister1());
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        // TODO ARM
    }

}
