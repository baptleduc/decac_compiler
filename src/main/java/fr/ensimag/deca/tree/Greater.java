package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, GPRegister regDest, boolean branchOnTrue, Label branchLabel) {
        if (branchOnTrue) {
            compiler.addInstruction(new BGT(branchLabel));
        } else {
            compiler.addInstruction(new BLE(branchLabel));
        }
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);
        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        GPRegister regLeft = leftDVal.codeGenToGPRegister(compiler);

        // Compare the two values
        compiler.addInstruction(new CMP(rightDVal, regLeft));
        if (branchOn){
            compiler.addInstruction(new BGT(label));
        }
        else{
            compiler.addInstruction(new BLE(label));
        }
        
    }

}
