
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import java.util.Iterator;

public class Constructor {
    private final Label label;
    private final ClassDefinition classDefinition;
    private static final RegisterOffset INSTANCE_OFFSET = new RegisterOffset(-2, Register.LB); // Registrer that
                                                                                               // contains the address
                                                                                               // of the start of the
                                                                                               // heap

    public Constructor(ClassDefinition classDefinition) {
        this.classDefinition = classDefinition;
        this.label = new Label("init." + classDefinition.getType().getName().getName(), false);
    }

    public Label getLabel() {
        return label;
    }

    private void initializeFieldToZero(FieldDefinition fieldDef, DecacCompiler compiler, int index) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), new RegisterOffset(index, compiler.getRegister1())));
    }

    private void initializeAllFieldsToZero(ClassDefinition classDef, DecacCompiler compiler) {

        EnvironmentExp classMembers = classDefinition.getMembers();
        Iterator<Symbol> it = classMembers.getSymbolCurrentEnvIterator();
        while (it.hasNext()) {
            Symbol symbol = it.next();
            ExpDefinition def = classMembers.get(symbol);
            try {
                FieldDefinition fieldDef = def.asFieldDefinition("Error", classDefinition.getLocation());
                int idxField = fieldDef.getIndex();
                initializeFieldToZero(fieldDef, compiler, idxField);
            } catch (ContextualError e) {
                // Not a method
            }
        }
    }

    private void initializeFieldExplicitly(FieldDefinition fieldDef, DecacCompiler compiler, int index) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(34), compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), new RegisterOffset(index, compiler.getRegister1())));
    }

    private void initializeAllFieldsExplicitly(ClassDefinition classDef, DecacCompiler compiler) {

        EnvironmentExp classMembers = classDefinition.getMembers();
        Iterator<Symbol> it = classMembers.getSymbolCurrentEnvIterator();
        while (it.hasNext()) {
            Symbol symbol = it.next();
            ExpDefinition def = classMembers.get(symbol);
            try {
                FieldDefinition fieldDef = def.asFieldDefinition("Error", classDefinition.getLocation());
                int idxField = fieldDef.getIndex();
                initializeFieldExplicitly(fieldDef, compiler, idxField);
            } catch (ContextualError e) {
                // Not a method
            }
        }
    }

    public void codeGenConstructor(DecacCompiler compiler) {
        compiler.addLabel(label);
        // TODO : handle stack overflow
        compiler.addInstruction(new LOAD(INSTANCE_OFFSET, compiler.getRegister1()));
        initializeAllFieldsToZero(classDefinition, compiler);
        compiler.addInstruction(new PUSH(compiler.getRegister1()));

        // Avoid BSR to the constructor of Object
        if (classDefinition.getSuperClass().getSuperClass() == null) {
            initializeAllFieldsExplicitly(classDefinition, compiler);
            compiler.addInstruction(new RTS());
            return;
        }
        
        compiler.addInstruction(
                new BSR(new Label("init." + classDefinition.getSuperClass().getType().getName().getName())));
        compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
        initializeAllFieldsExplicitly(classDefinition, compiler);
        compiler.addInstruction(new RTS());
    }

}