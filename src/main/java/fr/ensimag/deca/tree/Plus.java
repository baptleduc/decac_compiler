package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Plus extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(Plus.class);

    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

        getLeftOperand().codeGenInst(compiler);
        GPRegister regLeft = compiler.popUsedRegister();

        getRightOperand().codeGenInst(compiler);


        GPRegister regRight = compiler.popUsedRegister();

        compiler.addInstruction(new ADD(regRight, regLeft));
        compiler.pushAvailableRegister(regRight);
        compiler.pushUsedRegister(regLeft);
    }
}
