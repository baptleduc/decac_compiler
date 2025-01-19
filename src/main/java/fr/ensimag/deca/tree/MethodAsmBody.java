package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodAsmBody extends AbstractMethodBody {
    private static final Logger LOG = Logger.getLogger(MethodAsmBody.class);

    private AbstractStringLiteral asmCode;

    public MethodAsmBody(AbstractStringLiteral asmCode) {
        this.asmCode = asmCode;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    @Override
    public void verifyMethodBodyBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams,
            AbstractIdentifier methodClass, Type methodReturnType) throws ContextualError {
        return;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm(");
        asmCode.decompile(s);
        s.print(");");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmCode.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        asmCode.iter(f);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler, boolean hasReturn) {
        compiler.add(new InlinePortion(asmCode.getValue()));
    }

}
