package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
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
    public EnvironmentExp verifyListMethods(DecacCompiler compiler, AbstractIdentifier superClass)
            throws ContextualError {
        LOG.debug("verify listMethods: start");
        EnvironmentExp envExpR = new EnvironmentExp(null);
        int index = 0;
        for (AbstractDeclMethod declMethod : getList()) {
            EnvironmentExp envExp = declMethod.verifyMethod(compiler, superClass, index);
            // Check if envexp intersection is null
            for (Map.Entry<Symbol, ExpDefinition> entry : envExp.getCurrentEnvironment().entrySet()) {
                Symbol var = entry.getKey();
                ExpDefinition definition = entry.getValue();
                try {
                    envExpR.declare(var, definition); // add the key-value
                } catch (Exception e) {
                    // do nothing
                }
            }
            index = index + 1;
        }
        return envExpR;
        // LOG.debug("verify listClass: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
}
