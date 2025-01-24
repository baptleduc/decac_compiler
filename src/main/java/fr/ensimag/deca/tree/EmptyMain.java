package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import java.io.PrintStream;

/**
 * Empty main Deca program
 *
 * @author gl12
 * @date 01/01/2025
 */
public class EmptyMain extends AbstractMain {
    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        return;
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        compiler.addComment("Main program");
        compiler.addComment("Beginning of main instructions:");
        compiler.addInstruction(new HALT());
    }

    @Override
    protected void codeGenMainARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();
        program.addInstruction(new ARMInstruction("// Empty main program"));
    }

    /**
     * Contains no real information => nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // no main program => nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
