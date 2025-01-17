package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class Cast extends AbstractExpr {

    private AbstractIdentifier typeIdentifier;
    private AbstractExpr expressionToCast;

    public Cast(AbstractIdentifier typeIdentifier, AbstractExpr expressionToCast) {
        this.typeIdentifier = typeIdentifier;
        this.expressionToCast = expressionToCast;
    }

    public AbstractExpr getTypeIdentifier() {
        return typeIdentifier;
    }

    public AbstractExpr getExpressionToCast() {
        return expressionToCast;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeExpression = expressionToCast.verifyExpr(compiler, localEnv, currentClass);
        Type typeCast = typeIdentifier.verifyExpr(compiler, localEnv, currentClass);
        if(typeExpression.isVoid()){
            throw new ContextualError("Can't cast a void", expressionToCast.getLocation());
        }
        if((typeExpression.isInt() && typeCast.isFloat())
        ||(typeExpression.isFloat() && typeCast.isInt())){
            expressionToCast.setType(typeCast);
            return typeCast;
        }

        ClassType classTypeExpression = typeExpression.asClassType(" can only cast from a class type or int, float", expressionToCast.getLocation());
        ClassType classTypeCast = typeCast.asClassType(" can only cast to a class type or int, float", expressionToCast.getLocation());
        if((classTypeExpression.isSubClassOf(classTypeCast))
        || (classTypeCast.isSubClassOf(classTypeExpression))){
            expressionToCast.setType(classTypeCast);
            return classTypeCast;
        }
        else{
            throw new ContextualError("Can't cast those expressions", expressionToCast.getLocation());
        }

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        typeIdentifier.decompile(s);
        s.print(") ");
        s.print("(");
        expressionToCast.decompile(s);
        s.print(")");

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        typeIdentifier.iter(f);
        expressionToCast.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        typeIdentifier.prettyPrint(s, prefix, false);
        expressionToCast.prettyPrint(s, prefix, true);
    }

    @Override
    protected DVal getDVal(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
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
