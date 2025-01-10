package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type operandType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (operandType.isFloat()) {
            setType(operandType);
            return operandType;
        } else if (operandType.isInt()) {
            setType(operandType);
            return operandType;
        }
        throw new ContextualError(
                "Var " + operandType.getName() + " can't be used for 'UnaryMinus'",
                this.getOperand().getLocation());
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

}
