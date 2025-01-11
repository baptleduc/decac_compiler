package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

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

    abstract protected void codeGenOpBool(GPRegister dest, DVal source, DecacCompiler compiler);
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


}
