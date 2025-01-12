package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import java.io.PrintStream;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;
    private ImmediateInteger immediate;

    public BooleanLiteral(boolean value) {
        this.value = value;
        if (value) {
            this.immediate = new ImmediateInteger(1);
        } else {
            this.immediate = new ImmediateInteger(0);
        }

    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Retrieve the 'boolean' type from the predefined environment
        Type booleanType = compiler.environmentType.BOOLEAN;

        // Decorate the node with the 'boolean' type
        this.setType(booleanType);

        // Return the type of the expression
        return booleanType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
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
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    @Override
    protected boolean isImmediate() {
        return true;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        setDVal(immediate);
    }

}
