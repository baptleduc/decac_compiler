package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
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
    public EnvironmentExp verifyListMethods(DecacCompiler compiler) throws ContextualError {
        // LOG.debug("verify listMethods: start");
        // EnvironmentExp envExpR = new EnvironmentExp();
        // int index = 0;
        // for(AbstractDeclMethod declMethod : getList()){
        // EnvironmentExp envExp = declMethod.verifyMethod(compiler, index);
        // //Check if envexp intersection is null
        // for(Map.Entry<Symbol, ExpDefinition> entry :
        // envExp.currentEnvironment.entrySet()){
        // Symbol var = entry.getKey();
        // ExpDefinition definition = entry.getValue();
        // if(envExpR.currentEnvironment.containsKey(var)){
        // throw new ContextualError("Name of Method"+ var.getName()+ "already existing
        // in field environment", var.getLocation());
        // }
        // envExpR.declare(var, definition); // add the key-value
        // }
        // }

        // return envExpR;
        EnvironmentExp envExp = new EnvironmentExp(null);
        return envExp;
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
