package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class Return extends AbstractInst {
    private AbstractExpr returnExpr;

    public Return(AbstractExpr expr) {
        this.returnExpr = expr;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        if (returnType.isVoid()) {
            throw new ContextualError("can't return a void expression", returnExpr.getLocation());
        }
        returnExpr.setType(returnType);
        returnExpr.verifyRValue(compiler, localEnv, currentClass, returnType);
        return;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        returnExpr.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnExpr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        returnExpr.codeGenInst(compiler);
        DVal dval = returnExpr.getDVal(compiler);
        compiler.addInstruction(new LOAD(dval, compiler.getRegister0()));
        compiler.addInstruction(new BRA(compiler.getEndMethodLabel()));
    }
}
