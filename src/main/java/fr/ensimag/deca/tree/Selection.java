package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Represents a field selection (e.g., obj.field) in the Deca AST.
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class Selection extends AbstractLValue {
    private static final Logger LOG = Logger.getLogger(Selection.class);

    private AbstractExpr selectedObject;
    private AbstractIdentifier selectedField;

    public Selection(AbstractExpr selectedObject, AbstractIdentifier selectedField) {
        this.selectedObject = selectedObject;
        this.selectedField = selectedField;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("Start verify Selection:");
        LOG.debug("EnvironmentLocal Selection: " + localEnv.getCurrentEnvironment());
        ClassType typeClass2 = selectedObject.verifyExpr(compiler, localEnv, currentClass)
                .asClassType("only objects of class type have attributes", this.getLocation());
        ClassDefinition defEnv2 = (ClassDefinition) compiler.environmentType.defOfType(typeClass2.getName());
        EnvironmentExp envExp2 = defEnv2.getMembers();
        LOG.debug("Environement Local Selection : " + envExp2.getCurrentEnvironment());
        FieldDefinition selectedFieldDefinition = envExp2.get(selectedField.getName())
                .asFieldDefinition("lvalue must be a field definition", this.getLocation());
        selectedField.setDefinition(selectedFieldDefinition);
        if (selectedFieldDefinition.getVisibility().equals(Visibility.PUBLIC)) {
            setType(selectedFieldDefinition.getType());
            selectedField.setDefinition(selectedFieldDefinition);
            return selectedFieldDefinition.getType();
        } else if (selectedFieldDefinition.getVisibility().equals(Visibility.PROTECTED)) {
            if (typeClass2.isSubClassOf(currentClass.getType())
                    && (currentClass.getType().isSubClassOf(selectedFieldDefinition.getContainingClass().getType()))) {
                setType(selectedFieldDefinition.getType());
                selectedField.setDefinition(selectedFieldDefinition);
                return selectedFieldDefinition.getType();
            }
        }
        throw new ContextualError("Can't access to private attribute outside the class", this.getLocation());
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        selectedObject.iter(f);
        selectedField.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        selectedObject.prettyPrint(s, prefix, false);
        selectedField.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        selectedObject.decompile(s);
        s.print(".");
        selectedField.decompile(s);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        selectedObject.codeGenInst(compiler);
        try {
            int indexField = selectedField.getDefinition().asFieldDefinition(null, getLocation()).getIndex();
            DVal objectDVal = selectedObject.getDVal(compiler);
            DVal fieldDVal = selectedField.getDVal(compiler);
            LOG.debug("ObjectDVal: " + objectDVal);
            LOG.debug("FieldDVal: " + fieldDVal);

            GPRegister regObject = objectDVal.codeGenToGPRegister(compiler);
            compiler.addInstruction(new CMP(new NullOperand(), regObject));
            compiler.addInstruction(new BEQ(LabelManager.NULL_POINTER_ERROR.getLabel()));

            setDVal(new RegisterOffset(indexField, regObject));

        } catch (Exception e) {
            throw new DecacInternalError("Should not be called");
        }
    }

    @Override
    protected boolean isImmediate() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, Label label, boolean branchOn) {
        throw new DecacInternalError("Should not be called");
    }

}
