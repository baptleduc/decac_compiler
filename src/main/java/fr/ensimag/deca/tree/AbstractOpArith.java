package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
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
    abstract protected void codeGenOperationInst(GPRegister left, DVal right, DecacCompiler compiler);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        // Throw exception if the var has an unadapted type
        if (!(leftType.isInt() || leftType.isFloat())) {
            throw new ContextualError(
                    "Var" + leftType.getName() + " can't be used for arithmetical operation",
                    this.getLeftOperand().getLocation());
        } else if (!(rightType.isInt() || rightType.isFloat())) {
            throw new ContextualError(
                    "Var" + rightType.getName() + " can't be used for arithmetical operation",
                    this.getRightOperand().getLocation());
        }
        Type opType;
        if (rightType.isInt()) {
            if (leftType.isInt()) {
                opType = rightType; // int
            } else {
                opType = leftType; // float
                AbstractExpr rightFloat = new ConvFloat(this.getRightOperand());
                this.setRightOperand(rightFloat);
                this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        } else {
            if (leftType.isInt()) {
                AbstractExpr leftFloat = new ConvFloat(this.getLeftOperand());
                this.setLeftOperand(leftFloat);
                this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
            opType = rightType; // float
        }
        setType(opType);
        return opType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

        getLeftOperand().codeGenInst(compiler);
        GPRegister regLeft = compiler.popUsedRegister();

        AbstractExpr rightOperand = getRightOperand();
        if (rightOperand.isDVal()){
            DVal dval = getRightOperand().getDVal(compiler);
            codeGenOperationInst(regLeft, dval, compiler);
        }
        else{
            rightOperand.codeGenInst(compiler);
            GPRegister regRight = compiler.popUsedRegister();
            codeGenOperationInst(regLeft, regRight, compiler);
            compiler.pushAvailableRegister(regRight); // Free the register
        }

        compiler.pushUsedRegister(regLeft); // The result is in regLeft
    }
}
