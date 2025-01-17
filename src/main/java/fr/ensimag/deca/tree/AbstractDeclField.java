package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public abstract class AbstractDeclField extends Tree {

    private Visibility vision;
    private AbstractIdentifier type;

    public void setType(AbstractIdentifier typeParam) {
        type = typeParam;
    }

    public AbstractIdentifier getType() {
        return type;
    }

    public void setVisibility(Visibility visibility) {
        vision = visibility;
    }

    public Visibility getVisibility() {
        return vision;
    }
    
   /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public abstract EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superClassIdentifier, AbstractIdentifier classIdentifier) throws ContextualError;

   
   /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public abstract void verifyFieldsBody(DecacCompiler compiler, EnvironmentExp envExp, AbstractIdentifier classIdentifier) throws ContextualError;
    
}
