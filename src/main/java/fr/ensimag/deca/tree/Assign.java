package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.arm.instruction.ARMStore;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type ltype = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        setType(ltype);
        AbstractExpr convType = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, ltype);
        this.setRightOperand(convType);
        return ltype;
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        DVal leftDVal = getLeftOperand().getDVal(compiler);
        DVal rightDVal = getRightOperand().getDVal(compiler);
        GPRegister regRight = rightDVal.codeGenToGPRegister(compiler);

        DAddr destAddr;
        try {
            destAddr = (DAddr) leftDVal;
        } catch (ClassCastException e) {
            throw new DecacInternalError("Left operand should be an address");
        }

        compiler.addInstruction(new STORE(regRight, destAddr));
        setDVal(leftDVal);
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        getLeftOperand().codeGenInstARM(compiler);
        getRightOperand().codeGenInstARM(compiler);

        ARMProgram program = compiler.getARMProgram();

        int size = getRightOperand().getType().isFloat() ? ARMProgram.FLOAT_SIZE : ARMProgram.INT_SIZE;


        String rightReg;
        if (getRightOperand().isImmediate()) {
            if (getRightOperand().getType().isFloat()){
                rightReg = program.getAvailableRegisterTypeS();
                program.addInstruction(new ARMInstruction("fmov", rightReg, getRightOperand().getARMDVal().toString()));
            } else {
                rightReg = program.getAvailableRegister();
                program.addInstruction(new ARMInstruction("mov", rightReg, getRightOperand().getARMDVal().toString()));
            }
        } else {
            rightReg = getRightOperand().getARMDVal().toString();
        }
        program.addInstruction(new ARMStore(
                rightReg,
                getLeftOperand().getARMDVal().getVarName(),
                program, size));

        program.freeRegister(rightReg);
    }

}
