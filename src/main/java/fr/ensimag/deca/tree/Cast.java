package fr.ensimag.deca.tree;

import java.io.PrintStream;

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
            this.setType(typeCast);
            return typeCast;
        }
        if (typeExpression.sameType(typeCast)) {
            return typeCast;
        }
        if (typeExpression.sameType(typeCast)){
            return typeCast;
        }
        ClassType classTypeExpression = typeExpression.asClassType(" can only cast from a class type or int, float",
                expressionToCast.getLocation());
        ClassType classTypeCast = typeCast.asClassType(" can only cast to a class type or int, float",
                expressionToCast.getLocation());
        if ((classTypeExpression.isSubClassOf(classTypeCast))
                || (classTypeCast.isSubClassOf(classTypeExpression))) {
            this.setType(classTypeCast);
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
        Type type = typeIdentifier.getType(); // Target type for casting
        Type expressionType = expressionToCast.getType(); // Original type of the expression

        // Check for direct cast compatibility (i.e, same types)
        if (isDirectCast(type, expressionType)) {
            generateDirectCast(compiler);
            return;
        }

        // Check for numeric type conversions (i.e, int -> float or float -> int)
        if (isFloatToIntOrIntToFloat(type, expressionType)) {
            generateNumericCast(compiler, type, expressionType);
            return;
        }

        // General case: use instanceOf to verify type compatibility
        generateInstanceOfCast(compiler);
    }

    /**
     * Checks if the cast is directly compatible (e.g., same type or simple
     * boolean/int/float casting).
     *
     * @param type
     *            The target type for casting.
     * @param expressionType
     *            The original type of the expression.
     * @return true if the cast can be performed directly, false otherwise.
     */
    private boolean isDirectCast(Type type, Type expressionType) {
        return (type.isBoolean() || type.isInt() || type.isFloat()) && expressionType.sameType(type);
    }

    /**
     * Checks if the cast involves numeric types, specifically int to float or float
     * to int.
     *
     * @param type
     *            The target type for casting.
     * @param expressionType
     *            The original type of the expression.
     * @return true if the cast is a numeric conversion, false otherwise.
     */
    private boolean isFloatToIntOrIntToFloat(Type type, Type expressionType) {
        return (type.isFloat() && expressionType.isInt()) || (type.isInt() && expressionType.isFloat());
    }

    /**
     * Generates the code for direct cast cases where the types are directly
     * compatible.
     *
     * @param compiler
     *            The DecacCompiler instance managing the code generation.
     */
    private void generateDirectCast(DecacCompiler compiler) {
        expressionToCast.codeGenInst(compiler); // Generate the expression's code
        setDVal(expressionToCast.getDVal(compiler)); // Store the result as the value
    }

    /**
     * Generates the code for numeric type conversions, such as int to float or
     * float to int.
     *
     * @param compiler
     *            The DecacCompiler instance managing the code generation.
     * @param targetType
     *            The target type of the cast.
     * @param sourceType
     *            The source type of the expression.
     */
    private void generateNumericCast(DecacCompiler compiler, Type targetType, Type sourceType) {
        expressionToCast.codeGenInst(compiler);
        DVal exprDVal = expressionToCast.getDVal(compiler);
        GPRegister reg = exprDVal.codeGenToGPRegister(compiler);

        if (targetType.isFloat() && sourceType.isInt()) {
            compiler.addInstruction(new FLOAT(exprDVal, reg));
        } else if (targetType.isInt() && sourceType.isFloat()) {
            compiler.addInstruction(new INT(exprDVal, reg));
        }

        setDVal(reg);
    }

    /**
     * Generates the code for general cases requiring instanceOf verification.
     *
     * This method handles cases where the cast needs to be validated at runtime
     * using an
     * instanceOf check. If the check fails, the program will branch to an error
     * label.
     *
     * @param compiler
     *            The DecacCompiler instance managing the code generation.
     */
    private void generateInstanceOfCast(DecacCompiler compiler) {
        // Create an instanceOf check
        InstanceOf instanceOf = new InstanceOf(expressionToCast, typeIdentifier);
        Label elseCast = new Label("ElseCast"); // Label for invalid cast
        Label endCast = new Label("EndCast"); // Label for successful cast

        // Generate the instanceOf check
        instanceOf.codeGenBool(compiler, elseCast, false);
        expressionToCast.codeGenInst(compiler);
        compiler.addInstruction(new BRA(endCast));

        // Handle invalid cast
        compiler.addLabel(elseCast);
        compiler.addInstruction(new BRA(LabelManager.CAST_ERROR.getLabel()));

        // Finalize the casting process
        compiler.addLabel(endCast);
        setDVal(expressionToCast.getDVal(compiler));
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
