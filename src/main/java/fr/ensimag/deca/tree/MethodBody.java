package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodBody extends AbstractMethodBody {

    private static final Logger LOG = Logger.getLogger(MethodBody.class);

    private ListDeclVar listDeclVar;
    private ListInst listInst;

    public MethodBody(ListDeclVar listDeclVar, ListInst listInst) {
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    @Override
    public void verifyMethodBodyBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams,
            AbstractIdentifier methodClass, Type methodReturnType) throws ContextualError {
        EnvironmentExp localEnv = envExpParams;

        localEnv.setParent(envExp);
        listDeclVar.verifyListDeclVariable(compiler, localEnv, methodClass.getClassDefinition());
        EnvironmentExp empiledExp = localEnv.empile(localEnv.getParent());
        listInst.verifyListInst(compiler, empiledExp, methodClass.getClassDefinition(), methodReturnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        listDeclVar.decompile(s);
        listInst.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        listDeclVar.iter(f);
        listInst.iter(f);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler, boolean hasReturn) {
        compiler.startNewMethod();
        LOG.debug("Method body code generation: start :");

        LOG.debug(compiler.debugAvailableRegister());

        for (AbstractDeclVar declVar : listDeclVar.getList()) {
            declVar.codeGenDeclVarLoc(compiler);
        }
        for (AbstractInst inst : listInst.getList()) {
            inst.codeGenInst(compiler);
        }

        compiler.codeGenMethodPrologue();
        compiler.codeGenMethodEpilogue(hasReturn);
        compiler.addInstruction(new RTS());

        LOG.debug("Method body code generation: end");
        LOG.debug(compiler.debugAvailableRegister());
    }
}
