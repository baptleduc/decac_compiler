package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class DeclMethod extends AbstractDeclMethod {

    private AbstractIdentifier returnType;
    private AbstractIdentifier methodName;
    private ListDeclParam params;
    private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam params,
            AbstractMethodBody body) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.params = params;
        this.body = body;
    }

    // /**
    // * Pass 2 of [SyntaxeContextuelle]
    // */
    // @Override
    // protected void verifyMethod(DecacCompiler compiler) throws ContextualError {

    // //Get the name of the super class.
    // Symbol superName = superClass.getName();

    // //Check if the name of the super class exists in the environment
    // if(!compiler.environmentType.envTypes.containsKey(superName)){
    // throw new ContextualError("SuperClass is not in the environment",
    // superName.getLocation());
    // }
    // //Check if the name in the environment is a class and not a predef_type
    // else if(!compiler.environmentType.envTypes.get(superName).isClass()){
    // throw new ContextualError("SuperClass is not in the environment",
    // superName.getLocation());
    // }
    // //Check if envtype(name) is already defined
    // if (compiler.environmentType.envTypes.containsKey(nameClass)){
    // throw new ContextualError("Class with the same name already existing",
    // nameClass.getLocation());
    // }

    // //add the class to the environment
    // ClassType classType = new ClassType(nameClass.getName(),
    // nameClass.getLocation(), superClass.getDefinition());
    // ClassDefinition classDef = new ClassDefinition(classType,
    // nameClass.getLocation(), superClass.getDefinition());

    // // Baptiste
    // classType.setOperand(compiler.addGlobalVariable());
    // nameClass.setDefinition(classDef);

    // compiler.environmentType.envTypes.put(nameClass.getName(), classDef);

    // //Verify list_decl_field
    // fields.verifyDeclClass()

    // //Verify list_decl_method
    // methods.
    // }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" ... A FAIRE ... ");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
