package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodAsmBody extends AbstractMethodBody {

    private AbstractStringLiteral asmName;

    public MethodAsmBody(AbstractStringLiteral asmname) {
        asmName = asmname;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    @Override
    public void verifyMethodBodyBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, AbstractIdentifier methodClass, Type methodReturnType)throws ContextualError{
        return;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" ... A FAIRE ... ");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
