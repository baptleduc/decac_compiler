package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.MethodTable;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl12
 * @date 01/01/2025
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(DeclClass.class);

    private AbstractIdentifier classIdentifier;
    private AbstractIdentifier superClassIdentifier;
    private ListDeclField fields;
    private ListDeclMethod methods;

    public DeclClass(AbstractIdentifier classIdentifier, AbstractIdentifier superClassIdentifier, ListDeclField fields,
            ListDeclMethod methods) {
        Validate.notNull(classIdentifier);
        Validate.notNull(superClassIdentifier);
        Validate.notNull(fields);
        Validate.notNull(methods);

        this.classIdentifier = classIdentifier;
        this.superClassIdentifier = superClassIdentifier;
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
        Symbol superName = superClassIdentifier.getName();

        // Check if the name of the super class exists in the environment
        if (!compiler.environmentType.getEnvTypes().containsKey(superName)) {
            throw new ContextualError("SuperClass is not in the environment", superClassIdentifier.getLocation());
        }
        // Check if the name in the environment is a class and not a predef_type
        else if (!compiler.environmentType.getEnvTypes().get(superName).getType().isClass()) {
            throw new ContextualError("SuperClass is not in the environment", superClassIdentifier.getLocation());
        }

        // Check if envtype(name) is already defined
        if (compiler.environmentType.getEnvTypes().containsKey(classIdentifier.getName())) {
            throw new ContextualError("Class with the same name already existing", classIdentifier.getLocation());
        }
	
        // add the class to the environment
        TypeDefinition definitionSuper = compiler.environmentType.getEnvTypes().get(superName);
        ClassDefinition classDefinitionSuper = (ClassDefinition) definitionSuper;
        ClassType classType = new ClassType(classIdentifier.getName(), classIdentifier.getLocation(),
                classDefinitionSuper);
        ClassDefinition classDef = new ClassDefinition(classType, classIdentifier.getLocation(), null);
        classIdentifier.setDefinition(classDef);
        superClassIdentifier.setDefinition(definitionSuper);
        compiler.environmentType.declare(classIdentifier.getName(), classDef);
    }

    //  * Pass 2 of [SyntaxeContextuelle]
    //  */
     @Override
     protected void verifyClassMembers(DecacCompiler compiler)
             throws ContextualError {

    //     EnvironmentExp envExpSuper = superClass.getClassDefinition().getMembers();
    //     ClassDefinition currentClassDef = nameClass.getClassDefinition();
    //     currentClassDef.getMembers().empile(envExpSuper);

    //     EnvironmentExp envExpF = fields.verifyListFields();
    //     EnvironmentExp envExpM = fields.verifyListMethods();

    //     //Verify that envExpF and envExpM have no symb in common
    //     for(Map.Entry<Symbol, ExpDefinition> entry : envExpM.currentEnvironment.entrySet()){
    //         Symbol var = entry.getKey();
    //         if(envExpF.currentEnvironment.containsKey(var)){
    //             throw new ContextualError("Name of Method"+ var.getName()+ "declared in field environment", var.getLocation())
    //         }
    //     }

    //     //add symb of envExpM to envExpF
    //     for(Map.Entry<Symbol, ExpDefinition> entry : envExpM.currentEnvironment.entrySet()){
    //         Symbol var = entry.getKey();
    //         ExpDefinition definition = entry.getValue();
    //         envExpF.declare(var, definition); // add the key-value
    //     }

    //     currentClassDef.getMembers().empile(envExpF);
	 throw new UnsupportedOperationException("not yet implemented");
     }

        @Override
        protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
            throw new UnsupportedOperationException("not yet implemented");
        }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classIdentifier.prettyPrint(s, prefix, false);
        superClassIdentifier.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classIdentifier.iter(f);
        superClassIdentifier.iter(f);
        fields.iter(f);
        methods.iter(f);
    }

    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        MethodTable methodTable = new MethodTable(classIdentifier.getClassDefinition(),
                compiler.getLastMethodTableAddr());

        // Increment the last method table address by the number of methods in the class
        compiler.incrementLastMethodTableAddr(classIdentifier.getClassDefinition().getNumberOfMethods());

        methodTable.codeGenTable(compiler);
        LOG.debug(methodTable.toString());

    }
}

