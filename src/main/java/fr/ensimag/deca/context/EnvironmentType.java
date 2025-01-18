package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import java.util.HashMap;
import java.util.Map;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl12
 * @date 01/01/2025
 */
public class EnvironmentType {
    private final Map<Symbol, TypeDefinition> envTypes = new HashMap<Symbol, TypeDefinition>();

    public EnvironmentType(DecacCompiler compiler) {

        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

	Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));
	
        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.

        //
        // Define the Object class
        Symbol objectSymbol = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymbol, Location.BUILTIN, null);
        ClassDefinition objectDef = OBJECT.getDefinition();
        envTypes.put(objectSymbol, objectDef);

        // Add the equals method to the environmentExp
        Signature equalsSign = new Signature();
        equalsSign.add(OBJECT);
        MethodDefinition equalsDef = new MethodDefinition(BOOLEAN, Location.BUILTIN, equalsSign, 0);
        EnvironmentExp objectEnvExp = objectDef.getMembers();
        Symbol equalsSymbol = compiler.createSymbol("equals");
        try {
            objectEnvExp.declare(equalsSymbol, equalsDef);
        } catch (Exception e) {
            // nothing to do
        }
        objectDef.setNumberOfFields(0);
        objectDef.setNumberOfMethods(1);
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public final VoidType VOID;
    public final IntType INT;
    public final FloatType FLOAT;
    public final StringType STRING;
    public final BooleanType BOOLEAN;
    public final ClassType OBJECT;
    public final NullType NULL;
    
    public Map<Symbol, TypeDefinition> getEnvTypes() {
        return envTypes;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws IllegalArgumentException {
        if (envTypes.containsKey(name)) {
            throw new IllegalArgumentException("Key already exists");
        }
        // Hides the previous declaration
        this.envTypes.put(name, def);
    }
}
