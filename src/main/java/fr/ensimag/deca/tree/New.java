package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class New extends AbstractExpr {

    private AbstractIdentifier ident;
    
    public MethodeCall(AbstractIdentifier ident) {
	this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
	throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
	throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    String prettyPrintNode() {
	throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
	throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
	throw new UnsupportedOperationException("not yet implemented");
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

}
