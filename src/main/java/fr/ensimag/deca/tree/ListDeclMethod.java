package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclMethod.class);

    // /**
    // * Pass 2 of [SyntaxeContextuelle]
    // */
    // public EnvironmentExp verifyListMethods(DecacCompiler compiler) throws
    // ContextualError {
    // LOG.debug("verify listMethods: start");
    // EnvironmentExp envExpR = new EnvironmentExp();
    // for(AbstractDeclMethod declMethod : getList()){
    // EnvironmentExp envexp = declMethod.verifyMethod(compiler);
    // //Check if envtype(name) is already defined
    // if (compiler.environmentType.envTypes.containsKey(nameClass)){
    // throw new ContextualError("Class with the same name already existing",
    // nameClass.getLocation());
    // }
    // }
    // //faire l'union disjointe

    // return envExpR;
    // // LOG.debug("verify listClass: end");
    // }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod c : getList()) {
            c.decompile(s);
            s.println();
        }
    }
}
