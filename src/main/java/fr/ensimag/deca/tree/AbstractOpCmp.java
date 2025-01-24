package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if ((this instanceof AbstractOpExactCmp) && ((rightType.isBoolean() && leftType.isBoolean())
                || ((leftType.isClass() || leftType.isNull()) && (rightType.isClass() || rightType.isNull())))) {
            setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        } else if ((rightType.sameType(compiler.environmentType.INT)
                || rightType.sameType(compiler.environmentType.FLOAT))
                && (leftType.sameType(compiler.environmentType.INT)
                        || leftType.sameType(compiler.environmentType.FLOAT))) {
            setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError(rightType.getName() + " and " + leftType.getName() + " can't be compared",
                this.getRightOperand().getLocation());
    }

    protected abstract void codeGenBranch(DecacCompiler compiler, Label label, boolean branchOn);

    protected abstract String getARMCmpInverseAcronym();

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);
        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        GPRegister regLeft = leftDVal.codeGenToGPRegister(compiler);

        // Compare the two values
        compiler.addInstruction(new CMP(rightDVal, regLeft));
        codeGenBranch(compiler, label, branchOn);

        regLeft.freeGPRegister(compiler);
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        String leftRg;
        getLeftOperand().codeGenInstARM(compiler);
        ARMDVal leftOpDval = getLeftOperand().getARMDVal();
        if (getLeftOperand().isImmediate()) {
            leftRg = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", leftRg, leftOpDval.toString()));
        } else {
            leftRg = leftOpDval.toString();
        }

        String rightRg;
        getRightOperand().codeGenInstARM(compiler);
        ARMDVal rightOpDval = getRightOperand().getARMDVal();
        if (getRightOperand().isImmediate() && !getRightOperand().getARMDVal().isFloat()) {
            rightRg = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", rightRg, rightOpDval.toString()));
        } else {
            rightRg = rightOpDval.toString();
        }

        if (leftOpDval.isFloat()){
            String ldreg = program.getReadyRegister(getLeftOperand().getARMDVal());
            String rdreg = program.getReadyRegister(getRightOperand().getARMDVal());
            program.addInstruction(new ARMInstruction("fcmp", ldreg, rdreg));
            program.addInstruction(new ARMInstruction("cset", "w8", getARMCmpInverseAcronym()));
            program.freeRegisterTypeD(ldreg);
            program.freeRegisterTypeD(rdreg);
        } else {
            program.addInstruction(new ARMInstruction("subs", leftRg, leftRg, rightRg));
            program.addInstruction(new ARMInstruction("cset", leftRg, getARMCmpInverseAcronym()));
        }
        program.freeRegister(rightRg);
        setARMDVal(new ARMDVal(leftRg));

    }
}
