package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public class Cast extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Cast.class);

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
        LOG.debug("typeExpression: " + typeExpression);
        Type typeCast = typeIdentifier.verifyType(compiler);
        if (typeExpression.isVoid()) {
            throw new ContextualError("Can't cast a void", expressionToCast.getLocation());
        }
        if ((typeExpression.isInt() && typeCast.isFloat())
                || (typeExpression.isFloat() && typeCast.isInt())) {
            return typeCast;
        }

        ClassType classTypeExpression = typeExpression.asClassType(" can only cast from a class type or int, float",
                expressionToCast.getLocation());
        ClassType classTypeCast = typeCast.asClassType(" can only cast to a class type or int, float",
                expressionToCast.getLocation());
        if ((classTypeExpression.isSubClassOf(classTypeCast))
                || (classTypeCast.isSubClassOf(classTypeExpression))) {
            return classTypeCast;
        } else {
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
    protected void codeGenInst(DecacCompiler compiler) {
        Type type = typeIdentifier.getType();
        Type expressionType = expressionToCast.getType();

        if ((type.isBoolean() || type.isInt() || type.isFloat()) && expressionType.sameType(type)) {
            expressionToCast.codeGenInst(compiler);
            setDVal(expressionToCast.getDVal(compiler));
        
        } else if (type.isFloat() && expressionType.isInt()) {
            expressionToCast.codeGenInst(compiler);
            DVal exprDVal = expressionToCast.getDVal(compiler);
            GPRegister reg = exprDVal.codeGenToGPRegister(compiler);
            compiler.addInstruction(new FLOAT(exprDVal, reg));
            setDVal(reg);
        } else if (type.isInt() && expressionType.isFloat()) {
            expressionToCast.codeGenInst(compiler);
            DVal exprDVal = expressionToCast.getDVal(compiler);
            GPRegister reg = exprDVal.codeGenToGPRegister(compiler);
            compiler.addInstruction(new INT(exprDVal, reg));
            setDVal(reg);
        } else {
            InstanceOf instanceOf = new InstanceOf(expressionToCast, typeIdentifier);
            Label elseCast = new Label("ElseCast");
            Label endCast = new Label("EndCast");

            instanceOf.codeGenBool(compiler, elseCast, false);
            expressionToCast.codeGenInst(compiler);
            compiler.addInstruction(new BRA(endCast));

            compiler.addLabel(elseCast);
            compiler.addInstruction(new BRA(LabelManager.CAST_ERROR.getLabel()));

            compiler.addLabel(endCast);
            setDVal(expressionToCast.getDVal(compiler));
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
