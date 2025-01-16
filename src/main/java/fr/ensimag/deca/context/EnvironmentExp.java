package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 * 
 * Insertion (through method declare) is always done in the "current"
 * dictionary.
 * 
 * @author gl12
 * @date 01/01/2025
 */
public class EnvironmentExp {

    EnvironmentExp parentEnvironment;
    HashMap<Symbol, ExpDefinition> currentEnvironment;

    public EnvironmentExp getParent() {
        return parentEnvironment;
    }

    public HashMap<Symbol, ExpDefinition> getCurrentEnvironment() {
        return currentEnvironment;
    }

    public EnvironmentExp empile(EnvironmentExp env2) {
        if (env2 == null) {
            return this;
        }
        EnvironmentExp empiledEnv = this;
        for (Map.Entry<Symbol, ExpDefinition> entry : env2.currentEnvironment.entrySet()) {
            Symbol var = entry.getKey();
            ExpDefinition definition = entry.getValue();
            try {
                // Verify is the key is not in the current environment
                if (!this.currentEnvironment.containsKey(var)) {
                    empiledEnv.declare(var, definition); // add the key-value
                }
            } catch (DoubleDefException e) {
                // nothing to do
            }

        }

        return empiledEnv;
    }

    public void directSum(EnvironmentExp env2) throws DoubleDefException {
        // Verify that this and env2 have no symb in common
        for (Map.Entry<Symbol, ExpDefinition> entry : env2.getCurrentEnvironment().entrySet()) {
            Symbol var = entry.getKey();
            if (this.getCurrentEnvironment().containsKey(var)) {
                throw new DoubleDefException();
            }
        }

        // add symb of env2 to this
        for (Iterator<Map.Entry<Symbol, ExpDefinition>> it = env2.getCurrentEnvironment().entrySet().iterator(); it
                .hasNext();) {
            Map.Entry<Symbol, ExpDefinition> entry = it.next();
            Symbol var = entry.getKey();
            ExpDefinition definition = entry.getValue();
            try {
                declare(var, definition); // add the key-value
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.currentEnvironment = new HashMap<Symbol, ExpDefinition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        ExpDefinition defFromCurrent = this.currentEnvironment.get(key);
        if (defFromCurrent == null && this.parentEnvironment != null) {
            return this.parentEnvironment.get(key);
        }
        return defFromCurrent;
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
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (currentEnvironment.containsKey(name)) {
            throw new DoubleDefException();
        }
        // Hides the previous declaration
        this.currentEnvironment.put(name, def);
    }

    public Iterator<Symbol> getSymbolCurrentEnvIterator() {
        Set<Symbol> symbols = new LinkedHashSet<>();
        collectAllSymbols(symbols);
        return symbols.iterator();
    }

    private void collectAllSymbols(Set<Symbol> symbols) {
        symbols.addAll(currentEnvironment.keySet());
    }

}
