package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;

import java.io.PrintStream;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class Null extends AbstractExpr {

    public Null() {
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type nullType = compiler.environmentType.NULL;
        this.setType(nullType);
        return nullType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    String prettyPrintNode() {
        return "Null";
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
    protected DVal getDVal(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        setDVal(new NullOperand());
    }

    @Override
    protected boolean isImmediate() {
        return true;
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
