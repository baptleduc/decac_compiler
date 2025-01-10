package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

import org.apache.log4j.Logger;

/**
 * Integer literal
 *
 * @author gl12
 * @date 01/01/2025
 */
public class IntLiteral extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public int getValue() {
        return value;
    }

    private int value;
    private ImmediateInteger immediate;

    public IntLiteral(int value) {
        this.value = value;
        this.immediate = new ImmediateInteger(value);

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // Retrieve the 'int' type from the predefined environment
        Type intType = compiler.environmentType.INT;

        // Decorate the node with the 'int' type
        this.setType(intType);

        // Return the type of the expression
        return intType;
    }

    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    protected void codeGenInst(DecacCompiler compiler) {
        setDVal(immediate);
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        return immediate;
    }

    @Override
    protected boolean isImmediate() {
        return true;
    }

}
