package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!rightType.isInt()) {
            throw new ContextualError(
                    "Var" + rightType.getName() + " it not an int : modulo impossible",
                    this.getRightOperand().getLocation());
        } else if (!leftType.isInt()) {
            throw new ContextualError(
                    "Var" + leftType.getName() + " it not an int : modulo impossible",
                    this.getLeftOperand().getLocation());
        }
        setType(compiler.environmentType.INT);
        return compiler.environmentType.INT;
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected void codeGenOperationInst(GPRegister left, GPRegister right, DecacCompiler compiler) {
        compiler.addInstruction(new REM(right, left));
    }

}
