package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * Class that represents the method table of a class
 */
public class MethodTable {
    private LinkedList<Label> methods = new LinkedList<Label>();
    private ClassDefinition classDefinition;
    private DAddr lastMethodAddr;

    public MethodTable(ClassDefinition classDefinition, DAddr lastMethodAddr) {
        this.classDefinition = classDefinition;
        this.lastMethodAddr = lastMethodAddr;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // add name of the class
        sb.append("Method Table of class : " + classDefinition.getType().getName().getName() + "\n");
        for (int i = 0; i < methods.size(); i++) {
            sb.append(i + 1 + " : " + methods.get(i).toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * Add a method to the method table
     * 
     * @param label
     */
    private void addMethod(Label label) {
        methods.add(label);
    }

    /**
     * Build the method table of the class
     * 
     * @param classDefinition
     */
    private void buildTable(ClassDefinition classDefinition) {
        addMethod(LabelManager.OBJECT_EQUALS_LABEL.getLabel());

        EnvironmentExp classMembers = classDefinition.getMembers();
        Iterator<Symbol> it = classMembers.getSymbolIterator();

        while (it.hasNext()) {
            Symbol symbol = it.next();
            ExpDefinition def = classMembers.get(symbol);
            try {
                MethodDefinition methodDef = def.asMethodDefinition("Error", classDefinition.getLocation());
                addMethod(methodDef.getLabel());
            } catch (ContextualError e) {
                // Not a method
            }
        }
    }

    /*
     * CodeGen for the declaration of the Object class
     */
    public static void codeGenTableObjectClass(DecacCompiler compiler) {
        compiler.addComment("Method table for Object class");
        compiler.incrementOffsetGB(); // Increment offset to be at 1(GB)

        // Null pointer
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
     * Generate the instructions to build the method table
     * 
     * @param compiler
     */
    public void codeGenTable(DecacCompiler compiler) {
        buildTable(classDefinition);

        compiler.addComment("Method Table of class " + classDefinition.getType().getName().getName());

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
