package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;

/*
 * Class that represents the method table of a class
 */
public class MethodTable {
    private static final Logger LOG = Logger.getLogger(MethodTable.class);

    private ClassDefinition classDefinition;
    private String className;
    private ArrayList<Label> methods;

    public MethodTable(ClassDefinition classDefinition) {
        this.classDefinition = classDefinition;
        this.className = classDefinition.getType().getName().getName();
        this.methods = initializeMethods();
    }

    /**
     * Initializes the method table by allocating space for all methods
     * (including inherited methods) and setting default values.
     *
     * @return A list of labels representing the method table.
     */
    private ArrayList<Label> initializeMethods() {
        int totalMethods = classDefinition.getNumberOfMethods();
        LOG.debug("Initializing method table");
        LOG.debug("Number of methods : " + totalMethods);

        ArrayList<Label> methodsList = new ArrayList<>(totalMethods);
        fillListWithNull(methodsList, totalMethods);

        LOG.debug("Method table initialized: " + methodsList);
        return methodsList;
    }

    /**
     * Fills the given list with null values up to the specified size.
     *
     * @param list
     *            The list to be filled.
     * @param size
     *            The number of null values to add.
     */
    private static void fillListWithNull(ArrayList<Label> list, int size) {
        while (list.size() < size) {
            list.add(null);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // add name of the class
        sb.append("Method Table of class : " + className + "\n");
        for (int i = 0; i < methods.size(); i++) {
            sb.append(i + 1 + " : " + methods.get(i).toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * Adds a method to the table at the specified index.
     *
     * @param index
     *            The index where the method will be added.
     * @param className
     *            The name of the class defining the method.
     * @param methodName
     *            The name of the method.
     */
    private void addMethod(int index, String objectName, String methodName) throws DecacInternalError {
        LOG.debug("Object: " + objectName + " adding method : " + methodName + " at index : " + index);

        if (index < 0 || index >= methods.size()) {
            LOG.error("Index out of bounds: " + index);
            throw new DecacInternalError("Invalid index using for building method table of class + " + className);
        }

        LOG.debug("methods added");
        methods.set(index, new Label("code." + objectName + "." + methodName, false));
        LOG.debug(methods.toString());
    }

    /**
     * Adds all methods from the given environment to the method table.
     *
     * @param className
     *            The name of the class to which the methods belong.
     * @param classMembers
     *            The environment containing the methods to be added.
     */
    private void addMethods(String objectName, EnvironmentExp classMembers) {
        LOG.debug("Adding methods for class : " + objectName);
        Iterator<Symbol> it = classMembers.getSymbolCurrentEnvIterator();
        while (it.hasNext()) {
            Symbol symbol = it.next();
            LOG.debug("symbol : " + symbol.getName());
            ExpDefinition def = classMembers.get(symbol);
            try {
                MethodDefinition methodDef = def.asMethodDefinition("Error", classDefinition.getLocation());
                int idxTable = methodDef.getIndex();
                addMethod(idxTable, objectName, symbol.getName());
            } catch (ContextualError e) {
                // Not a method
            }
        }
    }

    /**
     * Builds the method table by traversing the inheritance chain and adding
     * methods.
     *
     * @param classDef
     *            The class definition for which the method table is built.
     */
    private void buildTable(ClassDefinition classDef, DecacCompiler compiler) {
        LOG.debug("Building method table for class : " + classDef.getType().getName().getName());
        ClassDefinition superClass = classDef.getSuperClass();

        if (superClass != null) {
            buildTable(superClass, compiler);
        }
        addMethods(classDef.getType().getName().getName(), classDef.getMembers());
    }

    /**
     * Generates the instructions to build the method table.
     *
     * @param compiler
     *            The Deca compiler instance used for code generation.
     */
    public void codeGenTable(DecacCompiler compiler) {
        buildTable(classDefinition, compiler);
        LOG.debug("Generating method table for class : " + className);
        compiler.addComment("Method Table of class " + className);

        DAddr lastMethodTableAddr = compiler.getLastMethodTableAddr();

        // Add Pointer to the last method table
        if (lastMethodTableAddr != null) {
            compiler.addInstruction(new LEA(lastMethodTableAddr, compiler.getRegister0()));
        } else {
            compiler.addInstruction(new LOAD(new NullOperand(), compiler.getRegister0())); // Object case, no pointer to
                                                                                           // last method table
        }
        compiler.incrementOffsetGB();
        compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
        compiler.setLastMethodTableAddr(compiler.getOffsetGB()); // Update the last method table address

        for (Label label : methods) {
            DVal labelDVal = new LabelOperand(label);
            compiler.addInstruction(new LOAD(labelDVal, compiler.getRegister0()));
            compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
            compiler.incrementOffsetGB();
        }
        classDefinition.setMethodTableAddr(lastMethodTableAddr); // Save the method table address in the class
                                                                 // definition
    }

}
