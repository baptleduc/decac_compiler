package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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

        if (this instanceof AbstractOpExactCmp) {
            setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        } else if ((rightType.sameType(compiler.environmentType.INT)
                || rightType.sameType(compiler.environmentType.FLOAT))
                && (leftType.sameType(compiler.environmentType.INT)
                        || leftType.sameType(compiler.environmentType.FLOAT))) {
            setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError("Vars " + rightType.getName() + " and " + leftType.getName() + " can't be compared",
                this.getRightOperand().getLocation());
    }

    protected abstract BranchInstruction getBranchInstruction(Label setTrueLabel);

    protected abstract String getARMCmpInverseAcronym();

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);
        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        Label endLabel = new Label("end");
        Label setTrueLabel = new Label("set_true");

        GPRegister regLeft = leftDVal.codeGenToGPRegister(compiler);

        // Compare the two values
        compiler.addInstruction(new CMP(rightDVal, regLeft));
        compiler.addInstruction(getBranchInstruction(setTrueLabel));

        // If the condition is not met, we load false
        compiler.addInstruction(new LOAD(0, regLeft));
        compiler.addInstruction(new BRA(endLabel));

        // If the condition is met, we load true
        compiler.addLabel(setTrueLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), regLeft));
        compiler.addInstruction(new BRA(endLabel));

        // End
        compiler.addLabel(endLabel);
        rightDVal.freeGPRegister(compiler);
        setDVal(regLeft);

    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        // we get a register with the value of the left operand (this operation is done
        // multiple time throught the projet, may be factorized)
        String leftRg;
        getLeftOperand().codeGenInstARM(compiler);
        ARMDVal leftOpDval = getLeftOperand().getARMDVal();
        if (getLeftOperand().isImmediate()) {
            leftRg = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", leftRg, leftOpDval.toString()));
        } else {
            leftRg = leftOpDval.toString();
        }

        // same with the right operand
        String rightRg;
        getRightOperand().codeGenInstARM(compiler);
        ARMDVal rightOpDval = getRightOperand().getARMDVal();
        if (getRightOperand().isImmediate()) {
            rightRg = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", rightRg, rightOpDval.toString()));
        } else {
            rightRg = rightOpDval.toString();
        }

        program.addInstruction(new ARMInstruction("subs", leftRg, leftRg, rightRg));
        program.addInstruction(new ARMInstruction("cset", leftRg, getARMCmpInverseAcronym()));
        program.freeRegister(rightRg);
        setARMDVal(new ARMDVal(leftRg)); // either 0 or 1

    }
}
