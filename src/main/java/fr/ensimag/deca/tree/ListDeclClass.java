package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 *
 * @author gl12
 * @date 01/01/2025
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass declClass : getList()) {
            declClass.verifyClass(compiler);
        }
        // LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListDeclClass(DecacCompiler compiler) {
        codeGenDeclClassObject(compiler);

        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenDeclClass(compiler);
        }
    }

    /*
     * CodeGen for the declaration of the Object class
     */
    private void codeGenDeclClassObject(DecacCompiler compiler) {
        compiler.addComment("Method table for Object class");
        compiler.addInstruction(new LOAD(new NullOperand(), compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
        compiler.incrementOffsetGB();
        DVal labelDVal = new LabelOperand(LabelManager.OBJECT_EQUALS_LABEL.getLabel());
        compiler.addInstruction(new LOAD(labelDVal, compiler.getRegister0()));
        compiler.addInstruction(new STORE(compiler.getRegister0(), compiler.getOffsetGB()));
        compiler.incrementOffsetGB();
    }

}
