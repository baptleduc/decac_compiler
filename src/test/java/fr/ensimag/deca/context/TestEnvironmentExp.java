package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the EnvironmentExp class.
 * These tests validate the behavior of the declare and get methods,
 * including handling of parent environments and double declarations.
 */
class TestEnvironmentExp {

    private EnvironmentExp globalEnv;
    private EnvironmentExp localEnv;
    private SymbolTable symbolTable;

    /**
     * Sets up the test environments before each test.
     * - Initializes the global and local environments.
     * - Creates a SymbolTable for managing symbols.
     */
    @BeforeEach
    void setUp() {
        // Create a real SymbolTable
        symbolTable = new SymbolTable();

        // Create global and local environments
        globalEnv = new EnvironmentExp(null); // Global environment (no parent)
        localEnv = new EnvironmentExp(globalEnv); // Local environment (with globalEnv as parent)
    }

    /**
     * Tests declaring a symbol in the current environment
     * and retrieving it from the same environment.
     */
    @Test
    void testDeclareAndGetInCurrentEnvironment() throws EnvironmentExp.DoubleDefException {
        // Create a symbol and a type
        SymbolTable.Symbol symbol = symbolTable.create("x");
        Type type = new IntType(symbolTable.create("int"));

        // Create a variable definition
        VariableDefinition definition = new VariableDefinition(type, null);

        // Declare the symbol in the global environment
        globalEnv.declare(symbol, definition);

        // Assert that the symbol can be retrieved from the global environment
        assertEquals(definition, globalEnv.get(symbol));
    }

    /**
     * Tests retrieving a symbol declared in the parent environment
     * from a child environment.
     */
    @Test
    void testDeclareAndGetInParentEnvironment() throws EnvironmentExp.DoubleDefException {
        // Create a symbol and a type
        SymbolTable.Symbol symbol = symbolTable.create("y");
        Type type = new IntType(symbolTable.create("int"));

        // Create a variable definition
        VariableDefinition definition = new VariableDefinition(type, null);

        // Declare the symbol in the global environment (parent)
        globalEnv.declare(symbol, definition);

        // Assert that the symbol can be retrieved from the local environment
        // via the parent environment
        assertEquals(definition, localEnv.get(symbol));
    }

    /**
     * Tests declaring a symbol in the local environment
     * that hides a symbol with the same name in the parent environment.
     */
    @Test
    void testDeclareInLocalEnvironmentHidesParentDefinition() throws EnvironmentExp.DoubleDefException {
        // Create a symbol and types
        SymbolTable.Symbol symbol = symbolTable.create("z");
        Type type = new IntType(symbolTable.create("int"));

        // Create two variable definitions
        VariableDefinition globalDefinition = new VariableDefinition(type, null);
        VariableDefinition localDefinition = new VariableDefinition(type, null);

        // Declare the symbol in the global environment
        globalEnv.declare(symbol, globalDefinition);

        // Declare the symbol in the local environment
        localEnv.declare(symbol, localDefinition);

        // Assert that the local definition hides the parent definition
        assertEquals(localDefinition, localEnv.get(symbol)); // Local environment returns local definition
        assertEquals(globalDefinition, globalEnv.get(symbol)); // Global environment remains unaffected
    }

    /**
     * Tests that declaring a symbol twice in the same environment
     * throws a DoubleDefException.
     */
    @Test
    void testDoubleDefinitionThrowsException() throws EnvironmentExp.DoubleDefException {
        // Create a symbol and a type
        SymbolTable.Symbol symbol = symbolTable.create("a");
        Type type = new IntType(symbolTable.create("int"));

        // Create a variable definition
        VariableDefinition definition = new VariableDefinition(type, null);

        // Declare the symbol in the global environment
        globalEnv.declare(symbol, definition);

        // Assert that declaring the same symbol again throws an exception
        assertThrows(EnvironmentExp.DoubleDefException.class, () -> {
            globalEnv.declare(symbol, new VariableDefinition(type, null));
        });
    }

    /**
     * Tests that declaring a symbol in the local environment
     * does not affect the parent environment.
     */
    @Test
    void testDeclareInLocalDoesNotAffectParent() throws EnvironmentExp.DoubleDefException {
        // Create a symbol and a type
        SymbolTable.Symbol symbol = symbolTable.create("b");
        Type type = new IntType(symbolTable.create("int"));

        // Create a variable definition
        VariableDefinition localDefinition = new VariableDefinition(type, null);

        // Declare the symbol in the local environment
        localEnv.declare(symbol, localDefinition);

        // Assert that the symbol is not present in the parent environment
        assertNull(globalEnv.get(symbol));

        // Assert that the symbol is present in the local environment
        assertEquals(localDefinition, localEnv.get(symbol));
    }
}
