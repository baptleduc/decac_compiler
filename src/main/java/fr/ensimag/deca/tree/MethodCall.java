package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import fr.ensimag.deca.context.ExpDefinition;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class MethodCall extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(MethodCall.class);

    private AbstractExpr leftOperand;
    private AbstractIdentifier rightOperand;
    private ListExpr params;

    public MethodCall(AbstractExpr leftOperand, AbstractIdentifier rightOperand, ListExpr params) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.params = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
	LOG.debug("Start verify MethodCall");
        ClassType classType2 = leftOperand.verifyExpr(compiler, localEnv, currentClass).asClassType(
                "method " + rightOperand.getName() + " can only be called on class types", leftOperand.getLocation());
        ClassDefinition classDef2 = (ClassDefinition)compiler.environmentType.defOfType(classType2.getName());
        EnvironmentExp envExp2 = classDef2.getMembers();
	LOG.debug("EnvironmentExp of " + classDef2 + " : " + envExp2.getCurrentEnvironment());
	ExpDefinition methodExpDef = envExp2.get(rightOperand.getName());
	if (methodExpDef == null){
	    throw new ContextualError(rightOperand.getName() + " is not defined as a method", rightOperand.getLocation());
	}
	MethodDefinition methodDef = methodExpDef.asMethodDefinition(rightOperand.getName() + " is not defined as a method", rightOperand.getLocation());
        Signature sig = methodDef.getSignature();
	int n = 0;
	for (AbstractExpr param : params.getList()) {
            param.verifyRValue(compiler, localEnv, currentClass, sig.paramNumber(n));
	    n++;
        }
	LOG.debug("End verify MethodCall");
	setType(methodDef.getType());
	rightOperand.setDefinition(methodDef);
        return methodDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
	if (leftOperand != null) {
	    leftOperand.decompile(s);
	    s.print(".");
	}
	rightOperand.decompile(s);
	s.print("(");
	params.decompile(s);
	s.print(")");
    }

    @Override
    String prettyPrintNode() {
	return "MethodCall";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
	if (leftOperand != null){
	    leftOperand.iter(f);   
	}
	rightOperand.iter(f);
	params.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
	if (leftOperand != null){
	    leftOperand.prettyPrint(s, prefix, false);
	}
	rightOperand.prettyPrint(s, prefix, false);
	params.prettyPrint(s, prefix, false);
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected boolean isImmediate() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
