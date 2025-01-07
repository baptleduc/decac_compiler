package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.context.VariableDefinition;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class DeclVar extends AbstractDeclVar {

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Verified that type is correct
        Type varType = type.verifyType(compiler);

        if (localEnv.get(varName.getName()) != null) {
            throw new ContextualError("Variable " + varName.getName() + " already declared in this context",
                    varName.getLocation());
        }

        // Add the variable to the environment
        VariableDefinition varDef = new VariableDefinition(varType, varName.getLocation());
        varName.setDefinition(varDef);
        try {
            localEnv.declare(varName.getName(), varDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable " + varName.getName() + " already declared in this context",
                    varName.getLocation());
        }

    }

    protected void codeGenDeclVar(DecacCompiler compiler) {
        ((VariableDefinition)varName.getDefinition()).setOperand(new DAddr(compiler.getStackManagement().));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
