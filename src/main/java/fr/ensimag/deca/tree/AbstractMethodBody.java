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
public abstract class AbstractMethodBody extends Tree {
    public abstract void verifyMethodBodyBody(DecacCompiler compiler, EnvironmentExp envExp,
            EnvironmentExp envExpParams, AbstractIdentifier methodClass, Type methodReturnType) throws ContextualError;

    protected abstract void codeGenMethodBody(DecacCompiler compiler);
}
