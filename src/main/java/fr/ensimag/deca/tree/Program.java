package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl12
 * @date 01/01/2025
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        LOG.debug("EnvironmentType start: " + compiler.environmentType.getEnvTypes());
        classes.verifyListClass(compiler);
        LOG.debug("EnvironmentType after pass 1: " + compiler.environmentType.getEnvTypes());
        classes.verifyListClassMembers(compiler);
        LOG.debug("EnvironmentType after pass 2: " + compiler.environmentType.getEnvTypes());
        classes.verifyListClassBody(compiler);
        LOG.debug("EnvironmentType program before main pass 3: " + compiler.environmentType.getEnvTypes());
        main.verifyMain(compiler);

        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        StackManager stackManager = compiler.getStackManager();
        // Passe 1: vtables
        classes.codeGenMethodTable(compiler);

        // Passe 2
        IMAProgram classesProgram = new IMAProgram();
        compiler.withProgram(classesProgram, () -> classes.codeGenClasses(compiler));

        IMAProgram mainIMAProgram = new IMAProgram();
        compiler.withProgram(mainIMAProgram, () -> main.codeGenMain(compiler));

        // Merge the main program with the general program
        stackManager.getProgram().append(mainIMAProgram);
        stackManager.getProgram().append(classesProgram);

        // Error handling
        compiler.checkStackOverflow();
        compiler.generateAllErrors();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
