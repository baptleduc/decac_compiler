package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclField extends AbstractDeclField {

    private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private AbstractInitialization init;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier name,
            AbstractInitialization init) {
        this.visibility = visibility;
        this.type = type;
        setType(type); // TODO : removes ?
        this.name = name;
        this.init = init;
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superClassIdentifier,
            AbstractIdentifier classIdentifier) throws ContextualError {
        Type fieldType = type.verifyType(compiler);
        if (fieldType.sameType(compiler.environmentType.VOID)) {
            throw new ContextualError("Can't declare a field " + name.getName() + " with void type",
                    type.getLocation());
        }
        EnvironmentExp envExpSuper = superClassIdentifier.getClassDefinition().getMembers();
        if (envExpSuper.getCurrentEnvironment().containsKey(name.getName())) {
            if (!envExpSuper.getCurrentEnvironment().get(name.getName()).isField()) {
                throw new ContextualError(name.getName() + " must be declared as a Field ", name.getLocation());
            }
        }
        FieldDefinition fieldDef = new FieldDefinition(fieldType, name.getLocation(), visibility,
                classIdentifier.getClassDefinition(), 1);
        EnvironmentExp environmentField = new EnvironmentExp(null);
        try {
            environmentField.declare(name.getName(), fieldDef);
        } catch (Exception e) {
            // do nothing
        }
        // Décoration du champ
        name.setDefinition(fieldDef);
        return environmentField;
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyFieldsBody(DecacCompiler compiler, EnvironmentExp envExp, AbstractIdentifier classIdentifier)
            throws ContextualError {
        Type fieldType = type.verifyType(compiler);
        init.verifyInitialization(compiler, fieldType, envExp, classIdentifier.getClassDefinition());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(visibility.toString() + " ");
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        init.decompile(s);
        s.println(";");
    }

    @Override
    String prettyPrintNode() {
        return "[visibility=" + visibility + "]" + " " + super.prettyPrintNode();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        getType().prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        init.iter(f);

    }

}
