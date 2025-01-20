package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class New extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(New.class);

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
        this.setType(newType);
        return newType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
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
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        ClassDefinition classDef = ident.getClassDefinition();
        DAddr methodTableAddr = classDef.getMethodTableAddr();
        GPRegister regDest = compiler.allocGPRegister();

        compiler.addInstruction(new NEW(classDef.getNumberOfFields() + 1, regDest)); // +1 for the method table
        compiler.addInstruction(new BOV(LabelManager.HEAP_OVERFLOW_ERROR.getLabel()));
        compiler.addInstruction(new LEA(methodTableAddr, compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), new RegisterOffset(0, regDest)));
        compiler.addInstruction(new PUSH(regDest));
        compiler.addInstruction(new BSR(LabelManager.getInitLabel(ident)));
        compiler.addInstruction(new POP(regDest));

        LOG.debug("New object created in register " + regDest);

        setDVal(regDest);
    }

    @Override
    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
