package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class DeclVar extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(DeclVar.class);

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

        if (varType.isVoid()) {
            throw new ContextualError(varName.getName() + " : can't declare var with type void",
                    varName.getLocation());
        }

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
        EnvironmentExp empiledEnv = localEnv.empile(localEnv.getParent());
        initialization.verifyInitialization(compiler, varType, empiledEnv, currentClass);

    }

    @Override
    protected void codeGenDeclVarGlob(DecacCompiler compiler) {
        DAddr addr = compiler.addGlobalVariable();
        ((VariableDefinition) varName.getDefinition()).setOperand(addr);
        initialization.codeGenInitialization(compiler, addr);
    }

    @Override
    protected void codeGenDeclVarLoc(DecacCompiler compiler) {
        DAddr addr = compiler.addLocalVariable();
        ((VariableDefinition) varName.getDefinition()).setOperand(addr);
        initialization.codeGenInitialization(compiler, addr);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        this.initialization.decompile(s);
        s.print(";");
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
