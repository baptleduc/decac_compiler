package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclField.class);

    // /**
    // * Pass 2 of [SyntaxeContextuelle]
    // */
    EnvironmentExp verifyListFields(DecacCompiler compiler, AbstractIdentifier superClassIdentifier,
            AbstractIdentifier classIdentifier) throws ContextualError {

        LOG.debug("verify listClass: start");
        EnvironmentExp envExpR = new EnvironmentExp(null);
        int numberOfFieldsSuperClass = ((ClassDefinition) (compiler.environmentType.getEnvTypes()
                .get(superClassIdentifier.getName()))).getNumberOfFields();
        ((ClassDefinition) (compiler.environmentType.getEnvTypes().get(classIdentifier.getName())))
                .setNumberOfFields(numberOfFieldsSuperClass);
        LOG.debug(
                "number of fields of superclass of " + classIdentifier.getName() + " : " + numberOfFieldsSuperClass);
        for (AbstractDeclField declField : getList()) {
            EnvironmentExp envExp = declField.verifyField(compiler, superClassIdentifier, classIdentifier);
            try {
                envExpR.directSum(envExp);
            } catch (Exception e) {
                throw new RuntimeException("Unable to merge environments due to a conflict.", e);
            }
        }
        LOG.debug("number of fields of " + classIdentifier.getName() + " : "
                + ((ClassDefinition) (compiler.environmentType.getEnvTypes().get(classIdentifier.getName())))
                        .getNumberOfFields());
        LOG.debug("verify listClass: end");
        return envExpR;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    void verifyListFieldsBody(DecacCompiler compiler, EnvironmentExp envExp, AbstractIdentifier classIdentifier)
            throws ContextualError {
        // LOG.debug("verify listClass: start");
        EnvironmentExp envExpR = new EnvironmentExp(null);
        for (AbstractDeclField declField : getList()) {
            declField.verifyFieldsBody(compiler, envExp, classIdentifier);
        }
        return;
        // LOG.debug("verify listClass: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
        }
    }
}
