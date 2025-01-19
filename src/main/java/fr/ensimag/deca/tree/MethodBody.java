package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodBody extends AbstractMethodBody {

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
    protected void codeGenMethodBody(DecacCompiler compiler) {
        for (AbstractDeclVar declVar : listDeclVar.getList()) {
            declVar.codeGenDeclVarLoc(compiler);
        }
        for (AbstractInst inst : listInst.getList()) {
            inst.codeGenInst(compiler);
        }
    }

}
