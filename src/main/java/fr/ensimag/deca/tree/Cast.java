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
import java.io.PrintStream;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class Cast extends AbstractExpr {

    private AbstractIdentifier typeIdentifier;
    private AbstractExpr expressionToCast;

    public Cast(AbstractIdentifier typeIdentifier, AbstractExpr expressionToCast) {
        this.typeIdentifier = typeIdentifier;
        this.expressionToCast = expressionToCast;
    }

    public AbstractExpr getTypeIdentifier() {
        return typeIdentifier;
    }

    public AbstractExpr getExpressionToCast() {
        return expressionToCast;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        typeIdentifier.decompile(s);
        s.print(") ");
        s.print("(");
        expressionToCast.decompile(s);
        s.print(")");

    }
    @Override
    protected void iterChildren(TreeFunction f) {
        typeIdentifier.iter(f);
        expressionToCast.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        typeIdentifier.prettyPrint(s, prefix, false);
        expressionToCast.prettyPrint(s, prefix, true);
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
