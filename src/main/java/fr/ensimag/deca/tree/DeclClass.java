package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl12
 * @date 01/01/2025
 */
public class DeclClass extends AbstractDeclClass {

    private AbstractIdentifier nameClass;
    private AbstractIdentifier superClass;
    private ListDeclField fields;
    private ListDeclMethod methods;

    public DeclClass(AbstractIdentifier nameClass, AbstractIdentifier superClass, ListDeclField fields,
            ListDeclMethod methods) {
        Validate.notNull(nameClass);
        Validate.notNull(superClass);
        Validate.notNull(fields);
        Validate.notNull(methods);

        this.nameClass = nameClass;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {

        // Get the name of the super class.
        Symbol superName = superClass.getName();

        // Check if the name of the super class exists in the environment
        if (!compiler.environmentType.getEnvTypes().containsKey(superName)) {
            throw new ContextualError("SuperClass is not in the environment", superClass.getLocation());
        }
        // Check if the name in the environment is a class and not a predef_type
        else if (!compiler.environmentType.getEnvTypes().get(superName).getType().isClass()) {
            throw new ContextualError("SuperClass is not in the environment", superClass.getLocation());
        }

        // Check if envtype(name) is already defined
        if (compiler.environmentType.getEnvTypes().containsKey(nameClass.getName())) {
            throw new ContextualError("Class with the same name already existing", nameClass.getLocation());
        }

        // add the class to the environment
        TypeDefinition definitionSuper = compiler.environmentType.getEnvTypes().get(superName);
        ClassDefinition classDefinitionSuper = (ClassDefinition) definitionSuper;
        ClassType classType = new ClassType(nameClass.getName(), nameClass.getLocation(), classDefinitionSuper);
        ClassDefinition classDef = new ClassDefinition(classType, nameClass.getLocation(), null);
        nameClass.setDefinition(classDef);
        superClass.setDefinition(definitionSuper);
        compiler.environmentType.declare(nameClass.getName(), classDef);
    }

    // /**
    // * Pass 2 of [SyntaxeContextuelle]
    // */
    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

        // EnvironmentExp envExpSuper = superClass.getClassDefinition().getMembers();
        // ClassDefinition currentClassDef = nameClass.getClassDefinition();
        // currentClassDef.getMembers().empile(envExpSuper);

        // EnvironmentExp envExpF = fields.verifyListFields();
        // EnvironmentExp envExpM = fields.verifyListMethods();

        // //Verify that envExpF and envExpM have no symb in common
        // for(Map.Entry<Symbol, ExpDefinition> entry :
        // envExpM.currentEnvironment.entrySet()){
        // Symbol var = entry.getKey();
        // if(envExpF.currentEnvironment.containsKey(var)){
        // throw new ContextualError("Name of Method"+ var.getName()+ "declared in field
        // environment", var.getLocation())
        // }
        // }

        // //add symb of envExpM to envExpF
        // for(Map.Entry<Symbol, ExpDefinition> entry :
        // envExpM.currentEnvironment.entrySet()){
        // Symbol var = entry.getKey();
        // ExpDefinition definition = entry.getValue();
        // envExpF.declare(var, definition); // add the key-value
        // }

        // currentClassDef.getMembers().empile(envExpF);
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        nameClass.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        nameClass.iter(f);
        superClass.iter(f);
        fields.iter(f);
        methods.iter(f);
    }
}
