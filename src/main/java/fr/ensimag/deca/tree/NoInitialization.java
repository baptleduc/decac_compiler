package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.arm.instruction.ARMStore;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import java.io.PrintStream;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl12
 * @date 01/01/2025
 */
public class NoInitialization extends AbstractInitialization {

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        return;
    }

    @Override
    public boolean isImplicit() {
        return true;
    }

    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void codeGenInitialization(DecacCompiler compiler, DAddr addr) {
        // nothing to do
    }

    @Override
    public void codeGenInitializationARM(DecacCompiler compiler, String varName, String type) {
        ARMProgram prog = compiler.getARMProgram();

        if (type.equals(".double")) {
            // float
            String reg = prog.getAvailableRegisterTypeS();
            prog.addInstruction(new ARMInstruction("fmov", reg, "#0.0"));
            prog.addInstruction(new ARMStore(reg, varName, prog, ARMProgram.FLOAT_SIZE));
            prog.freeRegisterTypeS(reg);
            return;
        }

        String reg = prog.getAvailableRegister();
        prog.addInstruction(new ARMInstruction("mov", reg, 0));
        prog.addInstruction(new ARMStore(reg, varName, prog));
        prog.freeRegister(reg);
    }

}
