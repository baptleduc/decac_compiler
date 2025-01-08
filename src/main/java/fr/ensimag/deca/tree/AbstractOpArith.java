package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * Generate assembly code for the operation between left and right operands
     * 
     * @param left
     * @param right
     * @param compiler
     */
    abstract protected void codeGenOperationInst(GPRegister left, GPRegister right, DecacCompiler compiler);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                
            Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            Type opType;
            if (rightType.isInt()) {
                if (leftType.isInt()) {
                    opType = rightType; // int
                } else {
                    opType = leftType; // float
                }
            } else {
                opType = rightType;
            }
            return opType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

        getLeftOperand().codeGenInst(compiler);
        GPRegister regLeft = compiler.popUsedRegister();

        getRightOperand().codeGenInst(compiler);
        GPRegister regRight = compiler.popUsedRegister();

        codeGenOperationInst(regLeft, regRight, compiler);

        compiler.pushAvailableRegister(regRight);
        compiler.pushUsedRegister(regLeft);
    }


}
