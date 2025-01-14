package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import java.io.PrintStream;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        return compiler.environmentType.INT;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenRead(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
    }

    @Override
    protected void codeGenBranch(DecacCompiler compiler, GPRegister reg, boolean branchOnTrue, Label branchLabel) {
        throw new DecacInternalError("Should not be called");
    }

}
