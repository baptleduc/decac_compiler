package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
	// rajouter la decoration de l'arbre
        if ((rightType.sameType(compiler.environmentType.INT) || rightType.sameType(compiler.environmentType.FLOAT))
                && (leftType.sameType(compiler.environmentType.INT)
                        || leftType.sameType(compiler.environmentType.FLOAT))) {
            return compiler.environmentType.BOOLEAN;
        } else if (this instanceof AbstractOpExactCmp && rightType.sameType(leftType)) {
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError("Vars " + rightType.getName() + " and " + leftType.getName() + " can't be compared",
                this.getRightOperand().getLocation());
    }

}
