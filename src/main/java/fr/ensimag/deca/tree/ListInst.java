package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author gl12
 * @date 01/01/2025
 */
public class ListInst extends TreeList<AbstractInst> {
    private static final Logger LOG = Logger.getLogger(ListInst.class);

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler
     *            contains "env_types" attribute
     * @param localEnv
     *            corresponds to "env_exp" attribute
     * @param currentClass
     *            corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *            corresponds to "return" attribute (void in the main bloc).
     */
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // Traverse each statement in the list and perform contextual verification
        for (AbstractInst inst : this.getList()) {
            inst.verifyInst(compiler, localEnv, currentClass, returnType);
        }

    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.codeGenInst(compiler);
            compiler.freeAllGPRegisters();
        }

    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }

    public AbstractInst getLast() {
        AbstractInst tmp = null;
        for (AbstractInst e : this.getList()) {
            tmp = e;
        }
        return tmp;
    }
}
