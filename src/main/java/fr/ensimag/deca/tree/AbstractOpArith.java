package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
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

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        //TODO: Check 
        Type leftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!leftOp.isInt() || !rightOp.isInt()) {
            throw new ContextualError("Operands of arithmetic operation must be of type int", this.getLocation());
        }
        this.setType(leftOp);
        return leftOp;
    }

}
