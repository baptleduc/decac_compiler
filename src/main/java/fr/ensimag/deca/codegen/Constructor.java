
package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tree.AbstractDeclField;
import fr.ensimag.deca.tree.AbstractInitialization;
import fr.ensimag.deca.tree.Initialization;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

public class Constructor {
    private final Label label;
    private final ClassDefinition classDefinition;
    private ListDeclField fields;
    private static final RegisterOffset INSTANCE_OFFSET = new RegisterOffset(-2, Register.LB); // Registrer that
                                                                                               // contains the address
                                                                                               // of the start of the
                                                                                               // heap

    public Constructor(ClassDefinition classDefinition, ListDeclField fields) {
        this.classDefinition = classDefinition;
        this.label = new Label("init." + classDefinition.getType().getName().getName(), false);
        this.fields = fields;
    }

    public Label getLabel() {
        return label;
    }

    private void initializeAllFieldsToZero(ClassDefinition classDef, DecacCompiler compiler) {
        for (AbstractDeclField field : fields.getList()) {
            IntLiteral zero = new IntLiteral(0);
            Initialization initToZero = new Initialization(zero);
            initToZero.codeGenInitialization(compiler, new RegisterOffset(field.getIndex(), compiler.getRegister1()));
        }
    }

    private void initializeAllFieldsExplicitly(ClassDefinition classDef, DecacCompiler compiler) {
        for (AbstractDeclField field : fields.getList()) {
            AbstractInitialization init = field.getInitialization();
            init.codeGenInitialization(compiler, new RegisterOffset(field.getIndex(), compiler.getRegister1()));
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
        } else {
            compiler.addInstruction(
                    new BSR(new Label("init." + classDefinition.getSuperClass().getType().getName().getName())));
            compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
            initializeAllFieldsExplicitly(classDefinition, compiler);

        }
        compiler.addInstruction(new RTS());
    }

}