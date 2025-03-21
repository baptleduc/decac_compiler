package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);

    private GPRegister regDest;
    private DVal sourceDVal; // source register or immediate value or Register Offset

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    protected void setRegDest(GPRegister newRegDest) {
        regDest = newRegDest;
    }

    protected void setSourceDVal(DVal newSourceDVal) {
        sourceDVal = newSourceDVal;
    }

    /**
     * Generate assembly code for the operation between left and right operands
     * 
     * @param dest
     * @param source
     * @param compiler
     */
    abstract protected void codeGenOperationInst(GPRegister dest, DVal source, DecacCompiler compiler);

    abstract protected void codeGenOperationInstARM(String dest, String left, AbstractExpr right,
            DecacCompiler compiler);

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

    /**
     * Optimizes the case where the left operand is an immediate value.
     * Only used for commutative operations, override for non-commutative
     * operations.
     * 
     * @param compiler
     *            The compiler used for code generation.
     * @param leftDVal
     *            The left operand.
     * @param rightDVal
     *            The right operand.
     */
    protected void optimizeLeftImmediate(DecacCompiler compiler, DVal leftDVal, DVal rightDVal) {
        regDest = rightDVal.codeGenToGPRegister(compiler);
        sourceDVal = leftDVal;
    }

    /**
     * Initializes the destination register and source operand.
     * 
     * @param compiler
     *            The compiler used for code generation.
     */
    private void initializeRegisters(DecacCompiler compiler) {
        boolean leftIsImmediate = getLeftOperand().isImmediate();
        boolean rightIsImmediate = getRightOperand().isImmediate();

        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        if (rightIsImmediate) { // Right operand is immediate
            regDest = leftDVal.codeGenToGPRegister(compiler);
            sourceDVal = rightDVal;
        } else if (leftIsImmediate) { // Left operand is immediate, optimize
            optimizeLeftImmediate(compiler, leftDVal, rightDVal);
        } else { // Both operands are not immediate, choose left operand as destination
            regDest = leftDVal.codeGenToGPRegister(compiler);
            sourceDVal = rightDVal;
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Generate code for the left and right operands
        getLeftOperand().codeGenInst(compiler);

        if (getLeftOperand().getDVal(compiler) == compiler.getRegister0()) { // Case when left operand is a result of
                                                                             // method Call
            LOG.debug("Left operand is in R0");
            GPRegister regResult = compiler.allocGPRegister();
            compiler.addInstruction(new LOAD(compiler.getRegister0(), regResult), "Left operand is in R0");
            getLeftOperand().setDVal(regResult);
        }

        getRightOperand().codeGenInst(compiler);

        // Initialize registers and operands for the operation
        initializeRegisters(compiler);

        // Generate the operation instruction
        codeGenOperationInst(regDest, sourceDVal, compiler);

        // Check for overflow
        if (getType().isFloat()) {
            compiler.addInstruction(new BOV(LabelManager.OVERFLOW_ERROR.getLabel()));
        }

        // Free the source operand if necessary
        sourceDVal.freeGPRegister(compiler);

        // Store the result in the destination DVal
        setDVal(regDest);
    }

    protected void codeGenFloatRegistersPreparationARM(ARMProgram prog, String dest) {
        String ldreg = prog.getReadyRegister(getLeftOperand().getARMDVal());
        String rdreg = prog.getReadyRegister(getRightOperand().getARMDVal());
        addFloatOpARM(prog, ldreg, rdreg);
        prog.addInstruction(new ARMInstruction("fcvt", dest, ldreg));
        prog.freeRegisterTypeD(ldreg);
        prog.freeRegisterTypeD(rdreg);
    }

    protected void addFloatOpARM(ARMProgram prog, String lr, String rr) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram prog = compiler.getARMProgram();
        AbstractExpr left = getLeftOperand();
        AbstractExpr right = getRightOperand();

        left.codeGenInstARM(compiler);
        right.codeGenInstARM(compiler);

        String regDest = left.getARMDVal().toString();

        if (left.isImmediate() && !left.getARMDVal().isFloat()) {
            regDest = compiler.getARMProgram().getAvailableRegister();
            prog.addInstruction(new ARMInstruction("mov", regDest, left.getARMDVal().toString()));
        }

        if (getLeftOperand().getType().isFloat()) {
            codeGenFloatRegistersPreparationARM(prog, regDest);
        } else {
            codeGenOperationInstARM(regDest, regDest, right, compiler);
        }

        setARMDVal(new ARMDVal(regDest));

        if (!right.isImmediate() && !left.isImmediate()) {
            compiler.getARMProgram().freeRegister(right.getARMDVal().toString());
        }
    }
}
