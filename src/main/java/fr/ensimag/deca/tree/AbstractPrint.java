package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDataType;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMDirectStore;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.arm.instruction.ARMStore;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import java.io.PrintStream;
import java.util.LinkedList;

import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // Check the arguments passed to print
        for (AbstractExpr arg : this.getArguments().getList()) { // getArguments() returns the list of arguments
            arg.verifyExpr(compiler, localEnv, currentClass);
            if (!(arg.getType().sameType(compiler.environmentType.INT) ||
                    arg.getType().sameType(compiler.environmentType.FLOAT) ||
                    arg.getType().sameType(compiler.environmentType.STRING))) {
                throw new ContextualError("Var " + arg.getType().getName() + " can't be printed", arg.getLocation());
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            if (printHex && (a.getType().isInt() || a.getType().isFloat())) {
                a.codeGenInst(compiler);
                DVal dval = a.getDVal(compiler);
                if (a.getType().isInt()) {
                    compiler.addInstruction(new FLOAT(dval, compiler.getRegister1()));
                } else {
                    compiler.addInstruction(new LOAD(dval, compiler.getRegister1()));
                }
                compiler.addInstruction(new WFLOATX());
            } else {
                a.codeGenPrint(compiler);
            }
        }
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        // we create the string format and we store it in the data section
        String format = "";
        LinkedList<AbstractExpr> abExp = new LinkedList<>();
        for (AbstractExpr a : getArguments().getList()) {
            if (a.isImmediate()) {
                a.codeGenInstARM(compiler);
                format += a.getARMDVal().getTrueVal(); // without the instruction decoration
                continue;
            } 
            if (a.getType().isInt()) {
                format += "%d";
            } else if (a.getType().isFloat()) {
                format += "%f";
            }
            abExp.add(a);
        }
        String stringName = program.addStringLine(format);

        // we put in sp the print parameters
        program.setPrintNbParametersIfSup(abExp.size());
        int offset = 0;
        for (AbstractExpr a : abExp) {
            a.codeGenInstARM(compiler);
            // String reg;
            // if (a.armIsVariable)
            program.addInstruction(new ARMDirectStore(a.getARMDVal().toString(), offset));
            program.freeRegister(a.getARMDVal().toString());
            offset += 8;
        }

        // we call _printf
        program.addInstruction(new ARMInstruction("adr", "X0", stringName));
        program.addInstruction(new ARMInstruction("bl", "_printf"));
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print");
        s.print(getSuffix());
        if (printHex) {
            s.print("x");
        }
        s.print("(");
        arguments.decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
