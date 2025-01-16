package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class New extends AbstractExpr {

    private AbstractIdentifier ident;

    public New(AbstractIdentifier ident) {
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type newType = ident.verifyType(compiler);
        if (!newType.isClass()) {
            throw new ContextualError("Must use a class, not a " + newType.getName() + " to create a new object",
                    ident.getLocation());
        }
        return newType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    String prettyPrintNode() {
        return "New";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        ClassDefinition classDef = ident.getClassDefinition();
        DAddr methodTableAddr = classDef.getMethodTableAddr();

        compiler.addInstruction(new NEW(classDef.getNumberOfFields() + 1, compiler.getRegister0())); // +1 for the method table
        compiler.addInstruction(new STORE(compiler.getRegister0(), methodTableAddr));
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
