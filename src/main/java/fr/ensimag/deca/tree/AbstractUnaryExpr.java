package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }

    private AbstractExpr operand;

    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    protected abstract String getOperatorName();

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        operand.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

    protected abstract void codeGenUnaryExpr(GPRegister regDest, DVal sourceDVal, DecacCompiler compiler);

    // return the new register
    protected abstract String codeGenUnaryExprARM(ARMDVal dval, ARMProgram program);

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getOperand().codeGenInst(compiler);
        DVal reg = getOperand().getDVal(compiler);
        GPRegister regDest = reg.codeGenToGPRegister(compiler);
        codeGenUnaryExpr(regDest, reg, compiler);
        setDVal(regDest);
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {

        getOperand().codeGenInstARM(compiler);
        String reg = codeGenUnaryExprARM(getOperand().getARMDVal(), compiler.getARMProgram());
        setARMDVal(new ARMDVal(reg));
    }

}
