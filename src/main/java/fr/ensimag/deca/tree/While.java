package fr.ensimag.deca.tree;

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
 *
 * @author gl12
 * @date 01/01/2025
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label startWhile = new Label("start_while");
        Label endLabel = new Label("end_while");

        compiler.addLabel(startWhile);
        condition.codeGenBool(compiler, endLabel, false);
        body.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(startWhile));

        compiler.addLabel(endLabel);
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        ARMProgram program = compiler.getARMProgram();

        String startWhileARM = program.createLabel();
        String endLabelARM = program.createLabel();

        program.addLabelLine(startWhileARM);
        condition.codeGenInstARM(compiler);

        String tbnzReg = condition.getARMDVal().toString().contains("s") ? "w8" : condition.getARMDVal().toString();
        program.addInstruction(new ARMInstruction("tbnz", tbnzReg, "#0", endLabelARM));
        body.codeGenListInstARM(compiler);
        program.addInstruction(new ARMInstruction("b", startWhileARM));
        program.addLabelLine(endLabelARM);

        program.freeRegister(condition.getARMDVal().toString());
    }

}
