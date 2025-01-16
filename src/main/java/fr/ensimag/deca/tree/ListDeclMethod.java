package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclMethod.class);

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public EnvironmentExp verifyListMethods(DecacCompiler compiler, AbstractIdentifier classIdentifier,
            AbstractIdentifier superClass)
            throws ContextualError {
        LOG.debug("verify listMethods: start");
        EnvironmentExp envExpR = new EnvironmentExp(null);
        // init number of methods
        int numberOfMethodsSuperClass = ((ClassDefinition) (compiler.environmentType.getEnvTypes()
                .get(superClass.getName()))).getNumberOfMethods();
        ((ClassDefinition) (compiler.environmentType.getEnvTypes().get(classIdentifier.getName())))
                .setNumberOfMethods(numberOfMethodsSuperClass);
        LOG.debug(
                "number of Methods of superclass of " + classIdentifier.getName() + " : " + numberOfMethodsSuperClass);
        for (AbstractDeclMethod declMethod : getList()) {
            EnvironmentExp envExp = declMethod.verifyMethod(compiler, classIdentifier, superClass);
            // Check if envexp intersection is null
            for (Map.Entry<Symbol, ExpDefinition> entry : envExp.getCurrentEnvironment().entrySet()) {
                Symbol var = entry.getKey();
                ExpDefinition definition = entry.getValue();
                try {
                    envExpR.declare(var, definition); // add the key-value
                } catch (Exception e) {
                    throw new ContextualError("Method " + var.getName() + "aldready declared in this class",
                            definition.getLocation());
                }
            }
        }
        LOG.debug("number of Methods of " + classIdentifier.getName() + " : "
                + ((ClassDefinition) (compiler.environmentType.getEnvTypes().get(classIdentifier.getName())))
                        .getNumberOfMethods());
        LOG.debug("verify listClass: end");
        return envExpR;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
}
