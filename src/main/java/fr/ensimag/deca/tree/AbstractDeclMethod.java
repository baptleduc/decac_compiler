package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public abstract class AbstractDeclMethod extends Tree {
    protected abstract EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier classIdentifier,
            AbstractIdentifier superClass)
            throws ContextualError;

    public abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, AbstractIdentifier methodClass)
            throws ContextualError;

    protected abstract void codeGenDeclMethod(DecacCompiler compiler);

}
