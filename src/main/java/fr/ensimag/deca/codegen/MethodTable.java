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

    private static final int OBJECT_EQUALS_INDEX = 0;

    private ClassDefinition classDefinition;
    private String className;
    private ArrayList<Label> methods;
    private DAddr lastMethodAddr;

    public MethodTable(ClassDefinition classDefinition, DAddr lastMethodAddr) {
        this.classDefinition = classDefinition;
        this.className = classDefinition.getType().getName().getName();
        this.methods = initializeMethods();
        this.lastMethodAddr = lastMethodAddr;
    }

    /**
     * Initializes the method table by allocating space for all methods
     * (including inherited methods) and setting default values.
     *
     * @return A list of labels representing the method table.
     */
    private ArrayList<Label> initializeMethods() {
        int totalMethods = getTotalNumberOfMethods(classDefinition) + 1; // +1 for "equals"
        LOG.debug("Initializing method table");
        LOG.debug("Number of methods : " + getTotalNumberOfMethods(classDefinition));

        ArrayList<Label> methodsList = new ArrayList<>(totalMethods);
        fillListWithNull(methodsList, totalMethods);
        methodsList.set(OBJECT_EQUALS_INDEX, LabelManager.OBJECT_EQUALS_LABEL.getLabel());

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

    /**
     * Recusivly calculate the total number of methods, including herited ones.
     */
    private int getTotalNumberOfMethods(ClassDefinition classDefinition) {
        if (classDefinition.getSuperClass() == null) {
            return 0;
        }
        return classDefinition.getNumberOfMethods() + getTotalNumberOfMethods(classDefinition.getSuperClass());
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

        if (methods.get(index) != null) {
            LOG.debug("Method already exists at index : " + index);
            return;
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
        Iterator<Symbol> it = classMembers.getSymbolCurrentEnvIterator();
        while (it.hasNext()) {
            Symbol symbol = it.next();
            ExpDefinition def = classMembers.get(symbol);
            try {
                MethodDefinition methodDef = def.asMethodDefinition("Error", classDefinition.getLocation());
                int idxTable = methodDef.getIndex() + 1; // +1 because the first method is equals
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
    private void buildTable(ClassDefinition classDef) {
        LOG.debug("Building method table for class : " + className);

        if (classDef.getSuperClass() != null) {
            buildTable(classDef.getSuperClass());
        }
        addMethods(classDef.getType().getName().getName(), classDef.getMembers());
    }

    /**
     * Generates the method table for the Object class.
     *
     * @param compiler
     *            The Deca compiler instance used for code generation.
     */
    public static void codeGenTableObjectClass(DecacCompiler compiler) {
        compiler.addComment("Method table for Object class");
        compiler.incrementOffsetGB(); // Increment offset to be at 1(GB)

        // Null pointer
        compiler.setLastMethodTableAddr(compiler.getOffsetGB());
        compiler.addInstruction(new LOAD(new NullOperand(), compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
        compiler.incrementOffsetGB(); // Increment offset to be at 2(GB)

        // Equals method
        DVal labelDVal = new LabelOperand(LabelManager.OBJECT_EQUALS_LABEL.getLabel());
        compiler.addInstruction(new LOAD(labelDVal, compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
        compiler.incrementOffsetGB(); // Increment offset to be at 3(GB)

    }

    /**
     * Generates the instructions to build the method table.
     *
     * @param compiler
     *            The Deca compiler instance used for code generation.
     */
    public void codeGenTable(DecacCompiler compiler) {
        buildTable(classDefinition);

        compiler.addComment("Method Table of class " + className);

        // Add Pointer to the last method table
        compiler.addInstruction(new LEA(lastMethodAddr, compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
        compiler.incrementOffsetGB();

        for (Label label : methods) {
            DVal labelDVal = new LabelOperand(label);
            compiler.addInstruction(new LOAD(labelDVal, compiler.getRegister0()));
            compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
            compiler.incrementOffsetGB();
        }
    }

}
