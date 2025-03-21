package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        Label endLabel = new Label("Or_end_label");
        if (branchOn) {
            getLeftOperand().codeGenBool(compiler, label, true);
            getRightOperand().codeGenBool(compiler, label, true);
        } else {
            getLeftOperand().codeGenBool(compiler, endLabel, true);
            getRightOperand().codeGenBool(compiler, label, false);
        }

        compiler.addLabel(endLabel);
    }

    @Override
    protected boolean endIfTrueARM() {
        return true;
    }

}
