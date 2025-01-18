package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 06/01/2025
 */
public class InstanceOf extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(InstanceOf.class);

    private AbstractExpr leftOperand;
    private AbstractIdentifier rightOperand;

    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type rightType = rightOperand.verifyType(compiler);
        if ((leftType.isClass() || leftType.isNull()) && rightType.isClass()) {
            leftOperand.setType(leftType);
            rightOperand.setType(rightType);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError("Can't use instanceof with this type " + rightOperand.getName(),
                rightOperand.getLocation());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        leftOperand.decompile(s);
        s.print(" instanceof ");
        rightOperand.decompile(s);
        s.print(")");
    }

    @Override
    String prettyPrintNode() {
        return "instanceof";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, false);
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal heapStartAddr = leftOperand.getDVal(compiler);
        LOG.debug("HeapStartAddr start addr: " + heapStartAddr);

    }

    @Override
    protected boolean isImmediate() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        leftOperand.codeGenInst(compiler);
        DVal heapStartAddr = leftOperand.getDVal(compiler);
        GPRegister regHeapStartAddr = heapStartAddr.codeGenToGPRegister(compiler);

        DVal methodTableAddrToCompare = rightOperand.getClassDefinition().getMethodTableAddr();
        Label labelInstanceOf = new Label("InstanceOf");
        compiler.addInstruction(new LOAD(new RegisterOffset(0, regHeapStartAddr), regHeapStartAddr));
        compiler.addLabel(labelInstanceOf);
        compiler.addInstruction(new CMP(new NullOperand(), regHeapStartAddr));
        compiler.addInstruction(new BEQ(label));

        compiler.addInstruction(new LOAD(new RegisterOffset(0, regHeapStartAddr), regHeapStartAddr),
                "Load method table addr"); // Load the address of the method table in R0
        compiler.addInstruction(new CMP(methodTableAddrToCompare, regHeapStartAddr));
        compiler.addInstruction(new BNE(labelInstanceOf));
    }
}
