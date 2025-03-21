package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Class declaration.
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public abstract class AbstractDeclParam extends Tree {
    public abstract Type verifyParamType(DecacCompiler compiler) throws ContextualError;

    public abstract EnvironmentExp verifyParamBody(DecacCompiler compiler) throws ContextualError;

    protected abstract void codeGenDeclParam(DecacCompiler compiler, int counterOffset);
}
