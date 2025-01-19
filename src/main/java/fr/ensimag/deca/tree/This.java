package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class This extends AbstractExpr {

    private boolean isImplicit; // set to true during parsing if the 'this' keyword is implicit

    public This(boolean isImplicit) {
        this.isImplicit = isImplicit;
    }

    public This() {
        this.isImplicit = false;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (currentClass == null || currentClass.getType().isNull()) {
            throw new ContextualError("Can not use this outside of a class", this.getLocation());
        }
        try {
            ClassType currentClassType = currentClass.getType().asClassType("can't use this outside of a class",
                    this.getLocation());
            this.setType(currentClassType);
            return currentClassType;
        } catch (Exception e) {
            throw new ContextualError("Can't use this outside of a class", this.getLocation());
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (isImplicit) {
            s.print("");
        } else {
            s.print("this");
        }
    }

    @Override
    String prettyPrintNode() {
        return "This";
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
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected boolean isImmediate() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
