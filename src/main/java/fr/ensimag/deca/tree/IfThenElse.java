package fr.ensimag.deca.tree;

import fr.ensimag.arm.ARMDVal;
import fr.ensimag.arm.ARMProgram;
import fr.ensimag.arm.instruction.ARMInstruction;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl12
 * @date 01/01/2025
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public void setElseBranch(ListInst newElseBranch) {
        this.elseBranch = newElseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label thenLabel = new Label("then");
        Label elseLabel = new Label("else");
        Label endLabel = new Label("end");

        condition.codeGenBool(compiler, elseLabel, false);

        compiler.addLabel(thenLabel);
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(endLabel));

        // Do the else branch
        compiler.addLabel(elseLabel);
        elseBranch.codeGenListInst(compiler);

        compiler.addLabel(endLabel);

    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();
        String ifLabel = program.createLabel();
        String elseLabel = program.createLabel();
        String finalLabel = program.createLabel();

        condition.codeGenInstARM(compiler);
        ARMDVal resultCondition = condition.getARMDVal(); // register containing either 1 or 0 (true or false)
        String tbnzReg = resultCondition.toString().contains("s") ? "w8" : resultCondition.toString();
        program.addInstruction(new ARMInstruction("tbnz", tbnzReg, "#0", elseLabel));
        program.addInstruction(new ARMInstruction("b", ifLabel));
        // then branch
        program.addLabelLine(ifLabel);
        thenBranch.codeGenListInstARM(compiler);
        program.addInstruction(new ARMInstruction("b", finalLabel));
        // else branch
        program.addLabelLine(elseLabel);
        elseBranch.codeGenListInstARM(compiler);
        program.addInstruction(new ARMInstruction("b", finalLabel));

        program.addLabelLine(finalLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
