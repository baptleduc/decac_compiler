package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

import static org.mockito.ArgumentMatchers.longThat;

import org.apache.log4j.Logger;

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
    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * Generate assembly code for the operation between left and right operands
     * 
     * @param dest
     * @param source
     * @param compiler
     */
    abstract protected void codeGenOperationInst(GPRegister dest, DVal source, DecacCompiler compiler);

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
        getRightOperand().codeGenInst(compiler);

        // boolean leftIsImmediate = getLeftOperand().isImmediate();
        // boolean rightIsImmediate = getRightOperand().isImmediate();

        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);
        LOG.debug("Left DVal: " + leftDVal.toString());
        LOG.debug("Right DVal: " + rightDVal.toString());
        assert(leftDVal != null && rightDVal != null);

        GPRegister regDest = rightDVal.codeGenToGPRegister(compiler);
        
        
        codeGenOperationInst(regDest, leftDVal, compiler);
        assert(regDest != null);
        setDVal(regDest);
        compiler.pushUsedRegister(regDest); // The result is in regLeft
    }
}
