package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
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

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verifyExpr IntLiteral: start");
        Symbol intSymbol = compiler.createSymbol("int");
        TypeDefinition intTypeDef = compiler.environmentType.defOfType(intSymbol);
        assert (intTypeDef != null);
        LOG.debug("verifyExpr IntLiteral: end");

        return intTypeDef.getType();
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
        GPRegister gpRegister = compiler.getStackManagement().getAvailableGPRegister(compiler);
        compiler.addInstruction(new LOAD(value, gpRegister));
    }

}
