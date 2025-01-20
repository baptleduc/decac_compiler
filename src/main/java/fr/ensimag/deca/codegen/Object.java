package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import org.apache.log4j.Logger;

public class Object {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    public static void codeGenClass(DecacCompiler compiler) {
        compiler.addComment("Code Object");
        codeGenObjectMethods(compiler);
    }

    public static void codeGenObjectMethods(DecacCompiler compiler) {
        Symbol equalsSymbol = compiler.createSymbol("equals");

        ExpDefinition equalsExpDefinition = compiler.environmentType.OBJECT.getDefinition().getMembers()
                .get(equalsSymbol);
        try {
            MethodDefinition equalsMethodDefinition = equalsExpDefinition.asMethodDefinition("equals method not found",
                    null);
            Label equalsLabel = equalsMethodDefinition.getLabel();
            compiler.addLabel(equalsLabel);
        } catch (Exception e) {
            // nothing to do
        }

        IMAProgram methodBody = new IMAProgram();
        compiler.withProgram(methodBody, () -> codeGenEqualsMethodBody(compiler));
        compiler.getProgram().append(methodBody);
    }

    private static void codeGenEqualsMethodBody(DecacCompiler compiler) {
        compiler.startNewMethod();
        LOG.debug("Generating code for equals method");
        LOG.debug(compiler.debugAvailableRegister());

        GPRegister regThisObject = compiler.allocGPRegister();
        GPRegister regOtherObject = compiler.allocGPRegister();

        Label returnFalseLabel = new Label("return_false");

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), regThisObject));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), regOtherObject));
        compiler.addInstruction(new CMP(regThisObject, regOtherObject));
        compiler.addInstruction(new BNE(returnFalseLabel));
        compiler.addInstruction(new LOAD(1, compiler.getRegister0()));
        compiler.addInstruction(new BRA(compiler.getEndMethodLabel()));

        compiler.addLabel(returnFalseLabel);
        compiler.addInstruction(new LOAD(0, compiler.getRegister0()));
        compiler.addInstruction(new BRA(compiler.getEndMethodLabel()));

        compiler.codeGenMethodPrologue();
        compiler.codeGenMethodEpilogue(true);
        regThisObject.freeGPRegister(compiler);
        regOtherObject.freeGPRegister(compiler);
        compiler.addInstruction(new RTS());

        LOG.debug("End of code generation for equals method");
        LOG.debug(compiler.debugAvailableRegister());
    }

}
