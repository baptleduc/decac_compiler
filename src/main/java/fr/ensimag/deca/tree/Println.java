package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMProgram;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments
     *            arguments passed to the print(...) statement.
     * @param printHex
     *            if true, then float should be displayed as hexadecimal (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
        compiler.addInstruction(new WNL());
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        // super.codeGenInstARM(compiler);
        // /**
        //  *  mov r0, #1               // File descriptor 1 (stdout)
        //     ldr r1, =newline         // Load address of newline
        //     mov r2, #1               // Length of the newline
        //     mov r7, #4               // Syscall number for sys_write
        //     svc #0                   // Trigger the syscall
        //  */
        // ARMProgram program = compiler.getARMProgram();
        // String stdoutReg = program.getAvailableRegister();
        // program.addInstructionARM("mov", stdoutReg, 1);
        // program.addInstructionARM("mov", "r0", "#10");
    }

    @Override
    String getSuffix() {
        return "ln";
    }
}
