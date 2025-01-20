package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tree.AbstractDeclField;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.AbstractInitialization;
import fr.ensimag.deca.tree.ListDeclField;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.apache.log4j.Logger;

public class Constructor {
    private static final Logger LOG = Logger.getLogger(Constructor.class);

    private final AbstractIdentifier classIdentifier;
    private final AbstractIdentifier superClassIdentifier;
    private final ClassDefinition classDefinition;
    private final ClassDefinition superClassDefinition;
    private final ListDeclField fields;
    private static final RegisterOffset INSTANCE_OFFSET = new RegisterOffset(-2, Register.LB); // Registrer that
                                                                                               // contains the address
                                                                                               // of the start of the
                                                                                               // heap

    public Constructor(AbstractIdentifier classIdentifer, AbstractIdentifier superClassIdentifier,
            ListDeclField fields) {
        this.classIdentifier = classIdentifer;
        this.superClassIdentifier = superClassIdentifier;
        this.classDefinition = classIdentifer.getClassDefinition();
        this.superClassDefinition = superClassIdentifier.getClassDefinition();
        this.fields = fields;
    }

    /**
     * Initializes all fields explicitly.
     *
     * @param classDef
     *            the class definition
     * @param compiler
     *            the compiler instance
     */
    private void initializeAllFieldsExplicitly(ClassDefinition classDef, DecacCompiler compiler) {
        for (AbstractDeclField field : fields.getList()) {
            AbstractInitialization init = field.getInitialization();
            AbstractIdentifier name = field.getName();
            int index = name.getFieldDefinition().getIndex();
            init.codeGenInitialization(compiler, new RegisterOffset(index, compiler.getRegister1()));
        }
    }

    /**
     * Initializes a field explicitly.
     *
     * @param compiler
     *            the compiler instance
     * @param fieldDef
     *            the field definition
     * @param init
     *            the initialization code
     * 
     *            Precondition: #0 in R0
     */
    private void initializeFieldExplicitly(DecacCompiler compiler, FieldDefinition fieldDef,
            AbstractInitialization init) {
        int index = fieldDef.getIndex();
        init.codeGenInitialization(compiler, new RegisterOffset(index, compiler.getRegister1()));
    }

    /**
     * Initializes a field to zero.
     *
     * @param compiler
     *            the compiler instance
     * @param fieldDef
     *            the field definition
     */
    private void initializeFieldToZero(DecacCompiler compiler, FieldDefinition fieldDef) {
        int index = fieldDef.getIndex();
        compiler.addInstruction(new STORE(compiler.getRegister0(), new RegisterOffset(index, compiler.getRegister1())));
    }

    /**
     * Initializes all fields to zero.
     *
     * @param compiler
     *            the compiler instance
     */
    private void initializeAllFieldsToZero(DecacCompiler compiler) {
        for (AbstractDeclField field : fields.getList()) {
            compiler.addInstruction(new LOAD(0, compiler.getRegister0()));
            initializeFieldToZero(compiler, field.getName().getFieldDefinition());
        }
    }

    /**
     * Initializes base class fields, i.e. fields of a class that extends Object.
     *
     * @param compiler
     *            the compiler instance
     */
    private void initBaseClassFields(DecacCompiler compiler) {
        boolean hasImplicitField = false;
        for (AbstractDeclField field : fields.getList()) {
            AbstractInitialization init = field.getInitialization();
            FieldDefinition fieldDef = field.getName().getFieldDefinition();
            if (init.isImplicit()) { // If the field is not explicitly initialized, initialize it to zero
                if (!hasImplicitField) { // Load 0 into R0 only once
                    compiler.addInstruction(new LOAD(0, compiler.getRegister0()));
                    hasImplicitField = true;
                }
                hasImplicitField = true;
                initializeFieldToZero(compiler, fieldDef);
            } else {
                initializeFieldExplicitly(compiler, fieldDef, init);
            }
        }
    }

    /**
     * Initializes fields of a class that extends another class.
     *
     * @param compiler
     *            the compiler instance
     */
    private void initExtendedClassFields(DecacCompiler compiler) {
        initializeAllFieldsToZero(compiler);

        Label labelInitSuperClass = LabelManager.getInitLabel(superClassIdentifier);
        compiler.addInstruction(new PUSH(compiler.getRegister1()));
        compiler.addInstruction(new BSR(labelInitSuperClass));
        compiler.addInstruction(new POP(compiler.getRegister1()));

        initializeAllFieldsExplicitly(classDefinition, compiler);
    }

    /**
     * Generates the code for the constructor of the class.
     *
     * @param compiler
     *            the compiler instance
     */
    public void codeGenConstructor(DecacCompiler compiler) {
        compiler.addLabel(LabelManager.getInitLabel(classIdentifier));
        if (classDefinition.getNumberOfFields() == 0) {
            compiler.addInstruction(new RTS());
            return;
        }

        if (superClassDefinition.getSuperClass() == null) { // Base class, class that extends Object
            compiler.addInstruction(new LOAD(INSTANCE_OFFSET, compiler.getRegister1()));
            initBaseClassFields(compiler);
        } else {
            compiler.addInstruction(new TSTO(3)); // one for having push R1, 2 for BSR to superClass constructor
            compiler.addInstruction(new BOV(LabelManager.STACK_OVERFLOW_ERROR.getLabel()));
            compiler.addInstruction(new LOAD(INSTANCE_OFFSET, compiler.getRegister1()));
            initExtendedClassFields(compiler);
        }
        compiler.addInstruction(new RTS());
    }

}