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
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import java.io.PrintStream;

/**
 * Represents a field selection (e.g., obj.field) in the Deca AST.
 * 
 * @author nicolmal
 * @date 13/01/2025
 */
public class Selection extends AbstractLValue {
    @Override
    protected void checkDecoration() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private AbstractExpr selectedObject;
    private AbstractIdentifier selectedField;

    public Selection(AbstractExpr selectedObject, AbstractIdentifier selectedField) {
        this.selectedObject = selectedObject;
        this.selectedField = selectedField;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        ClassType typeClass2 = selectedObject.verifyExpr(compiler, localEnv, currentClass)
                .asClassType("only objects of class type have attributes", this.getLocation());
        ClassDefinition defEnv2 = typeClass2.getDefinition();
        EnvironmentExp envExp2 = defEnv2.getMembers();
        FieldDefinition selectedFieldDefinition = envExp2.get(selectedField.getName())
                .asFieldDefinition("lvalue must be a field definition", this.getLocation());
        if (selectedFieldDefinition.getVisibility().equals(Visibility.PUBLIC)) {
            return selectedFieldDefinition.getType();
        } else if (selectedFieldDefinition.getVisibility().equals(Visibility.PROTECTED)) {
            if (typeClass2.isSubClassOf(currentClass.getType())
                    && (currentClass.getType().isSubClassOf(selectedFieldDefinition.getType().asClassType(
                            "can access a protected field only in daughters classes", selectedField.getLocation())))) {
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
        DVal objectDVal = selectedObject.getDVal(compiler);
        DVal fieldDVal = selectedField.getDVal(compiler);

        GPRegister regObject = objectDVal.codeGenToGPRegister(compiler);
        compiler.addInstruction(new CMP(new NullOperand(), regObject));
        compiler.addInstruction(new BSR(LabelManager.NULL_POINTER_ERROR.getLabel()));

        setDVal(fieldDVal);
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
