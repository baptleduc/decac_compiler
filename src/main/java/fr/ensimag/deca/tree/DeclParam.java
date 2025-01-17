package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclParam extends AbstractDeclParam {

    private AbstractIdentifier paramType;
    private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier paramType, AbstractIdentifier paramName) {
        this.paramType = paramType;
        this.paramName = paramName;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public EnvironmentExp verifyParamBody(DecacCompiler compiler) throws ContextualError {
        Type actualParamType = paramType.verifyType(compiler);
        ParamDefinition paramDef = new ParamDefinition(actualParamType, paramName.getLocation());
        EnvironmentExp environmentParam = new EnvironmentExp(null);
        try {
            environmentParam.declare(paramName.getName(), paramDef);
        } catch (Exception e) {
            // do nothing
        }
        paramName.setDefinition(paramDef);
        return environmentParam;
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public Type verifyParamType(DecacCompiler compiler) throws ContextualError {
        Type actualParamType = paramType.verifyType(compiler);
        if (actualParamType.sameType(compiler.environmentType.VOID)) {
            throw new ContextualError("Can't declare a param with void type", paramType.getLocation());
        }
        return actualParamType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        paramType.decompile(s);
        s.print(" ");
        paramName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        paramType.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        paramType.iter(f);
        paramName.iter(f);
    }

}
