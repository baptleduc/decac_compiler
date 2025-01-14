package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type operandType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!operandType.isBoolean()) {
            throw new ContextualError(
                    "Var " + operandType.getName() + " can't be used for 'Not'",
                    this.getOperand().getLocation());
        }
        setType(operandType);
        return operandType;
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenUnaryExpr(GPRegister regDest, DVal sourceDVal, DecacCompiler compiler) {
        throw new DecacInternalError("Should not be called");
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        getOperand().codeGenBool(compiler, label, !branchOn);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label endLabel = new Label("end_label");
        GPRegister reg = compiler.allocGPRegister();
        compiler.addInstruction(new LOAD(1, reg)); // We initialize the result to true
        codeGenBool(compiler, endLabel, true);

        compiler.addInstruction(new LOAD(0, reg));
        compiler.addLabel(endLabel);
        setDVal(reg);
    }

}
