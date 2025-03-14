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
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl12
 * @date 01/01/2025
 */
public class Initialization extends AbstractInitialization {
    private static final Logger LOG = Logger.getLogger(Initialization.class);

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    public boolean isImplicit() {
        return false;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        expression = expression.verifyRValue(compiler, localEnv, currentClass, t);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    public void codeGenInitialization(DecacCompiler compiler, DAddr addr) {
        expression.codeGenInst(compiler);
        DVal dval = expression.getDVal(compiler);
        GPRegister regDest = dval.codeGenToGPRegister(compiler);

        compiler.addInstruction(new STORE(regDest, addr));
        regDest.freeGPRegister(compiler);
        LOG.debug("STORE " + regDest + "," + addr);
    }

    private void immediateFloatInitializationARM(ARMProgram program, String varName) {
        String sRg = expression.getARMDVal().toString();
        program.addInstruction(new ARMStore(sRg, varName, program, ARMProgram.FLOAT_SIZE));
        program.freeRegisterTypeS(sRg);
    }

    @Override
    public void codeGenInitializationARM(DecacCompiler compiler, String varName, String type) {
        expression.codeGenInstARM(compiler);
        ARMProgram prog = compiler.getARMProgram();
        if (expression.isImmediate()) {

            if (expression.getType().isFloat()) {
                immediateFloatInitializationARM(prog, varName);
                return;
            }

            String reg = prog.getAvailableRegister();
            prog.addInstruction(new ARMInstruction("mov", reg, expression.getARMDVal().toString()));
            prog.addInstruction(new ARMStore(reg, varName, prog));
            prog.freeRegister(reg);
        } else {
            String reg = expression.getARMDVal().toString();
            prog.addInstruction(new ARMStore(reg, varName, prog));
            prog.freeRegister(reg);
        }
    }
}
