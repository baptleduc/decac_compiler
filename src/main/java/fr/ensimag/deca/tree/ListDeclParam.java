package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DirectSumException;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    private static final Logger LOG = Logger.getLogger(ListDeclParam.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam c : getList()) {
            c.decompile(s);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    Signature verifyListParams(DecacCompiler compiler) throws ContextualError {
        Signature sign = new Signature();
        for (AbstractDeclParam param : getList()) {
            Type paramType = param.verifyParamType(compiler);
            sign.add(paramType);
        }
        return sign;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    EnvironmentExp verifyListParamsBody(DecacCompiler compiler) throws ContextualError {
        EnvironmentExp envExpR = new EnvironmentExp(null);
        for (AbstractDeclParam param : getList()) {
            EnvironmentExp envExp = param.verifyParamBody(compiler);
            try {
                envExpR.directSum(envExp);
            } catch (DirectSumException e) {
                throw new ContextualError(
                        "Parameter " + e.getName() + " aldready declare as a parameter in this method",
                        e.getLocation());
            }
        }
        return envExpR;
    }

    public void codeGenDeclParams(DecacCompiler compiler) {
        int counterOffset = -2; // We start at -2(LB) because it is the object addr
        for (AbstractDeclParam param : getList()) {
            param.codeGenDeclParam(compiler, --counterOffset);
        }
    }

}
