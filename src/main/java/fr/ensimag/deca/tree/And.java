package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
    protected void codeGenOpBool(GPRegister dest, DVal source, DecacCompiler compiler){

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        GPRegister regLeft = leftDVal.codeGenToGPRegister(compiler);

        Label setFalse = new Label("set_false");
        Label end = new Label("end");
        
        
        compiler.addInstruction(new CMP(new ImmediateInteger(0), regLeft));
        compiler.addInstruction(new BEQ(setFalse));
        GPRegister regRight = rightDVal.codeGenToGPRegister(compiler);
        compiler.addInstruction(new CMP(0, regRight));
        compiler.addInstruction(new BEQ(setFalse));
        
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), regLeft));
        compiler.addInstruction(new BRA(end));

        compiler.addLabel(setFalse);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), regLeft));

        compiler.addLabel(end);
        setDVal(regLeft);

    }

}
