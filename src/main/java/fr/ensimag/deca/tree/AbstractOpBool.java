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
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if ((rightType.isBoolean() && leftType.isBoolean())) {
            setType(compiler.environmentType.BOOLEAN);

            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError(
                "Vars " + rightType.getName() + " and " + leftType.getName()
                        + "must be boolean values for logical operations (AND, OR)",
                this.getRightOperand().getLocation());
    }

    /**
     * Generates code for boolean operations.
     * 
     * @param compiler
     *            The Deca compiler instance.
     * @param jumpOnTrue
     *            Whether to jump on true (used for OR) or false (used for AND).
     * @param setLabel
     *            Label to jump to when the condition is met.
     */
    protected void codeGenBooleanOperation(DecacCompiler compiler, boolean jumpOnTrue, Label setLabel) {
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);

        Label endLabel = new Label("end");

        GPRegister regLeft = leftDVal.codeGenToGPRegister(compiler);

        // Check left operand
        compiler.addInstruction(new CMP(new ImmediateInteger(0), regLeft));
        compiler.addInstruction(jumpOnTrue ? new BNE(setLabel) : new BEQ(setLabel),
                "If " + regLeft.toString() + " is " + (jumpOnTrue ? "true" : "false") + ", branch to "
                        + setLabel.toString());

        // Check right operand
        GPRegister regRight = rightDVal.codeGenToGPRegister(compiler);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), regRight));
        compiler.addInstruction(jumpOnTrue ? new BNE(setLabel) : new BEQ(setLabel),
                "If " + regRight.toString() + " is " + (jumpOnTrue ? "true" : "false") + ", branch to "
                        + setLabel.toString());

        // Load result value in Regleft (0 for OR, 1 for AND)
        compiler.addInstruction(new LOAD(new ImmediateInteger(jumpOnTrue ? 0 : 1), regLeft));
        compiler.addInstruction(new BRA(endLabel));

        // Set value when condition is met
        compiler.addLabel(setLabel);
        compiler.addInstruction(new LOAD(new ImmediateInteger(jumpOnTrue ? 1 : 0), regLeft));

        // End
        compiler.addLabel(endLabel);
        setDVal(regLeft);
    }

    abstract protected boolean endIfTrueARM();

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        // si le premier est faux et que c'est and on branche sur final avec 0 (deja
        // dedans), si premier est vrai et que c'est or on branche sur final avec 1
        // (deja dedans)
        // sinon on branche sur le label deuxième opérande et dans celui ci on se
        // contente de mettre le résultat dans le registre puis de brancher sur le final

        String secondOperandLabel = program.createLabel();
        String finalLabel = program.createLabel();
        String labelIfTrue = endIfTrueARM() ? secondOperandLabel : finalLabel;
        String labelIfFalse = endIfTrueARM() ? finalLabel : secondOperandLabel;

        getLeftOperand().codeGenInstARM(compiler);
        String rgLeft;
        if (getLeftOperand().isImmediate()) {
            rgLeft = program.getAvailableRegister();
            program.addInstruction(new ARMInstruction("mov", rgLeft, getLeftOperand().getARMDVal().toString()));
        } else {
            rgLeft = getLeftOperand().getARMDVal().toString();
        }

        String tbnzReg = rgLeft.contains("s") ? "w8" : rgLeft;
        program.addInstruction(new ARMInstruction("tbnz", tbnzReg, "#0", labelIfTrue));
        program.addInstruction(new ARMInstruction("b", labelIfFalse));
        program.addLabelLine(secondOperandLabel);
        getRightOperand().codeGenInstARM(compiler);
        program.addInstruction(new ARMInstruction("mov", rgLeft, getRightOperand().getARMDVal().toString()));
        program.addInstruction(new ARMInstruction("b", finalLabel));
        program.addLabelLine(finalLabel);

        if (!getRightOperand().isImmediate()) {
            program.freeRegister(getRightOperand().getARMDVal().toString());
        }
        setARMDVal(new ARMDVal(rgLeft));
    }

}
