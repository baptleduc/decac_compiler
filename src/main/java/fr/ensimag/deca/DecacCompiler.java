package fr.ensimag.deca;

import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import net.bytebuddy.dynamic.scaffold.MethodGraph.Linked;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl12
 * @date 01/01/2025
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        if (compilerOptions == null || this.compilerOptions.getRegisters() == -1) {
            this.stackManager = new StackManager(program, Register.getMaxGPRegisters());
        } else {
            this.stackManager = new StackManager(program, this.compilerOptions.getRegisters());
        }
        this.source = source;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    public StackManager getStackManager() {
        return stackManager;
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();

    private Label endMethodLabel = new Label("end_method");

    /**
     * Stack management to handle registers and stack.
     */
    private final StackManager stackManager;

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();
    public final EnvironmentType environmentType = new EnvironmentType(this);

    public Symbol createSymbol(String name) {
        return symbolTable.create(name);
    }

    /**
     * Temporarily switches the current IMAProgram to the specified program
     * for the duration of the provided runnable task. After the task is executed,
     * the previous program is restored.
     *
     * @param program
     *            the temporary IMAProgram to use during the execution of the
     *            runnable
     * @param r
     *            the task to be executed with the temporary program
     */
    public void withProgram(IMAProgram program, Runnable r) {
        IMAProgram oldProgram = this.program;
        this.program = program;
        r.run();
        this.program = oldProgram;
    }

    public IMAProgram getProgram() {
        return program;
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addFirst(fr.ensimag.ima.pseudocode.Instruction,,java.lang.String)
     */
    public void addFirst(Instruction i, String comment) {
        program.addFirst(i, comment);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addFirst(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void addFirst(Instruction i) {
        program.addFirst(i);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     *      java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    /**
     * @see
     *      fr.ensimag.ima.pseudocode.ErrorManager#generateAllErrors()
     */
    public void generateAllErrors() {
        ErrorManager.generateAllErrors(this);
    }

    /**
     * Inserts a `TSTO` instruction to check for stack overflow and calculates the
     * required stack size.
     * Adds code at the start to check for overflow and at the end to handle the
     * error, unless `noVerify` is true.
     *
     * @param noVerify
     *            if true, skips adding the stack overflow error-handling code
     */
    public void checkStackOverflow() {
        if (getCompilerOptions().getNoCheck()) {
            return;
        }

        LOG.debug("Inserting TSTO instruction");
        int d = stackManager.calculateTSTOSize();
        ImmediateInteger imm = new ImmediateInteger(stackManager.getOffsetGBValue());

        // Insert TSTO instruction to test for stack overflow at the beginning of the
        // program
        addFirst(new ADDSP(imm)); // Increment SP by offsetGB
        addFirst(new BOV(LabelManager.STACK_OVERFLOW_ERROR.getLabel())); // Branch to stack overflow error if overflow
        addFirst(new TSTO(d), stackManager.getCommentTSTO());
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#addGlobalVariable()
     */
    public RegisterOffset addGlobalVariable() {
        return stackManager.addGlobalVariable();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#addLocalVariable()
     */
    public RegisterOffset addLocalVariable() {
        return stackManager.addLocalVariable();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#IncrementOffsetGB()
     */
    public void incrementOffsetGB() {
        stackManager.incrementOffsetGB();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#IncrementOffsetGB(int value)
     */
    public void incrementOffsetGB(int value) {
        stackManager.incrementOffsetGB(value);
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#getOffsetGB()
     */
    public RegisterOffset getOffsetGB() {
        return stackManager.getOffsetGB();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#getLastMethodTableAddr()
     */
    public DAddr getLastMethodTableAddr() {
        return stackManager.getLastMethodTableAddr();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#setLastMethodTableAddr()
     */
    public void setLastMethodTableAddr(DAddr addr) {
        stackManager.setLastMethodTableAddr(addr);
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#incrementLastMethodTableAddr(int
     *      value)
     */
    public void incrementLastMethodTableAddr(int value) {
        stackManager.incrementLastMethodTableAddr(value);
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#getLastUsedRegister()
     */
    public GPRegister getLastUsedRegister() {
        return stackManager.getLastUsedRegister();
    }

    public void freeRegister(GPRegister reg) {
        if (reg.getNumber() == 0 || reg.getNumber() == 1) {
            return;
        }
        stackManager.pushAvailableGPRegister(reg);
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#debugAvailableRegister()
     */
    public String debugAvailableRegister() {
        return stackManager.debugAvailableRegister();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#debugUsedRegister()
     */
    public String debugUsedRegister() {
        return stackManager.debugUsedRegister();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#getRegister1()
     */
    public GPRegister getRegister1() {
        return stackManager.getRegister1();
    }

    /**
     * @see
     *      fr.ensimag.deca.codegen.StackManager#getRegister0()
     */
    public GPRegister getRegister0() {
        return stackManager.getRegister0();
    }

    /*
     * Allocates a general-purpose register (GPRegister) for use by the compiler.
     * If there are no available registers, it saves the last used register onto
     * the stack and returns it.
     */
    public GPRegister allocGPRegister() {
        if (stackManager.isAvailableGPRegisterEmpty()) {
            GPRegister reg = stackManager.getLastUsedRegister();
            saveRegister(reg);
            stackManager.markRegisterUsedMethod(reg);
            return reg;
        }
        GPRegister reg = stackManager.popAvailableGPRegister();
        stackManager.pushUsedGPRegister(reg);
        stackManager.markRegisterUsedMethod(reg);
        return reg;
    }

    public void freeAllGPRegisters(){
        while (!stackManager.getUsedGPRegisters().isEmpty()) {
            GPRegister reg = stackManager.popUsedRegister();
            reg.freeGPRegister(this);
        }
    }

    private int calculateTSTOMethod() {
        return stackManager.getUsedRegistersMethod().size() + stackManager.getOffsetLBValue()
                + 2 * stackManager.getNumMethodCall();
    }

    public void incrementNumMethodCall() {
        stackManager.incrementNumMethodCall();
    }

    public void codeGenMethodPrologue() {
        for (int regIndex : stackManager.getUsedRegistersMethod()) {
            addFirst(new PUSH(Register.getR(regIndex)));
        }
        addFirst(new BOV(LabelManager.STACK_OVERFLOW_ERROR.getLabel()));
        addFirst(new TSTO(calculateTSTOMethod()));
    }

    public void codeGenMethodEpilogue(boolean hasReturn) {
        if (hasReturn) {
            addInstruction(new BRA(LabelManager.NO_RETURN_ERROR.getLabel()));
        }
        addLabel(endMethodLabel);
        while (!stackManager.getUsedRegistersMethod().isEmpty()) {
            int regIndex = stackManager.popUsedRegisterMethod();
            addInstruction(new POP(Register.getR(regIndex)));
        }
    }

    public void startNewMethod() {
        stackManager.initStackForMethod();
        endMethodLabel = LabelManager.getEndMethodLabel();
    }

    public Label getEndMethodLabel() {
        return endMethodLabel;
    }

    /**
     * Saves the given register onto the stack by pushing it and marks it as
     * available for reuse.
     * Updates the list of available and used registers index.
     * NOTE:
     *
     * @param reg
     *            the register to be saved onto the stack
     */
    private void saveRegister(GPRegister reg) {
        LOG.debug("Saving register " + reg.toString());
        stackManager.incrementNumSavedRegisters();
        stackManager.pushAvailableGPRegister(stackManager.popUsedRegister());
        addInstruction(new PUSH(reg), "Save register " + reg.toString());
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.replaceAll("\\.deca$", ".ass");
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName
     *            name of the source (deca) file
     * @param destName
     *            name of the destination (assembly) file
     * @param out
     *            stream to use for standard output (output of decac -p)
     * @param err
     *            stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert (prog.checkAllLocations());

        if (this.compilerOptions.getParse()) {
            // PrintStream printDecompile = new PrintStream();
            IndentPrintStream indentPrintDecompile = new IndentPrintStream(err);
            prog.decompile(indentPrintDecompile);
            return false;
        }
        prog.verifyProgram(this);
        assert (prog.checkAllDecorations());
        if (getCompilerOptions().getVerify() == true) {
            return false;
        }

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName
     *            Name of the file to parse
     * @param err
     *            Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError
     *             When an error prevented opening the source file
     * @throws DecacInternalError
     *             When an inconsistency was detected in the
     *             compiler.
     * @throws LocationException
     *             When a compilation error (incorrect program)
     *             occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

}
