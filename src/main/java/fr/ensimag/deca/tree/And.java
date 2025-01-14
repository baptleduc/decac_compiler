package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import static org.mockito.ArgumentMatchers.floatThat;

import org.apache.log4j.Logger;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class And extends AbstractOpBool {
    private static final Logger LOG = Logger.getLogger(And.class);

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }


    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        Label endLabel = new Label("And_end_label");
        if (branchOn){
            getLeftOperand().codeGenBool(compiler, endLabel, false);
            getRightOperand().codeGenBool(compiler, label, true);
        }
        else{
            getLeftOperand().codeGenBool(compiler, label, false);
            getRightOperand().codeGenBool(compiler, label, false);
        }


        compiler.addLabel(endLabel);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label endLabel = new Label("end_label");
        GPRegister reg = compiler.allocGPRegister();
        compiler.addInstruction(new LOAD(1, reg)); // We initialize the result to true
        codeGenBool(compiler, endLabel, true);
        
        compiler.addInstruction(new LOAD(0, reg));
        compiler.addLabel(endLabel);
        setDVal(reg);
    }


    @Override
    protected void codeGenBranch(DecacCompiler compiler, GPRegister reg, boolean branchOnTrue, Label branchLabel) {
        throw new DecacInternalError("Should not be called");
    }

}
