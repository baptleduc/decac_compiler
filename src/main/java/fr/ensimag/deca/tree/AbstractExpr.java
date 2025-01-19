package fr.ensimag.deca.tree;

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
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl12
 * @date 01/01/2025
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     *         in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed
     * by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    protected void setDVal(DVal dval) {
        this.dval = dval;
    }

    private Type type;
    private DVal dval = null; // Register, Immediate or d(XX)

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler
     *            (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *            is null in the main bloc.
     * @return the Type of the expression
     *         (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler
     *            contains the "env_types" attribute
     * @param localEnv
     *            corresponds to the "env_exp" attribute
     * @param currentClass
     *            corresponds to the "class" attribute
     * @param expectedType
     *            corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
        Type rvalueType = verifyExpr(compiler, localEnv, currentClass);
        if (rvalueType.sameType(expectedType)) {
            setType(rvalueType);
            return this;
        } else if (expectedType.isFloat() && rvalueType.isInt()) {
            AbstractExpr rValueConv = new ConvFloat(this);
            rValueConv.verifyExpr(compiler, localEnv, currentClass);
            return rValueConv;
        } else if ((rvalueType.isClass() || rvalueType.isNull()) && expectedType.isClass()) {

            ClassType classTypeExpected = expectedType.asClassType(" the var can't be assigned to this class",
                    this.getLocation());
            if (rvalueType.isNull()) {
                this.setType(rvalueType);
                return this;
            }
            ClassType classTypeRvalue = rvalueType.asClassType(" need to assign to a compatible class",
                    this.getLocation());

            if ((classTypeRvalue.isSubClassOf(classTypeExpected))) {
                this.setType(classTypeExpected);
                return this;
            }
            throw new ContextualError(classTypeRvalue.getName() + " is not subclass of " + classTypeExpected.getName(),
                    getLocation());
        }
        throw new ContextualError("Expected type " + expectedType + " but found type " + rvalueType, getLocation());
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type exprType = verifyExpr(compiler, localEnv, currentClass);
        setType(compiler.environmentType.BOOLEAN);
        if (exprType.isBoolean()) {
            setType(compiler.environmentType.BOOLEAN);
            return;
        }
        setType(compiler.environmentType.BOOLEAN);
        throw new ContextualError("Condition must be booleanType", this.getLocation());
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {

        if (getType().isInt()) {
            codeGenInst(compiler);
            DVal dval = getDVal(compiler);
            compiler.addInstruction(new LOAD(dval, compiler.getRegister1())); // TODO Change to get DAddr
            compiler.addInstruction(new WINT());
        } else if (getType().isFloat()) {
            codeGenInst(compiler);
            DVal dval = getDVal(compiler);
            compiler.addInstruction(new LOAD(dval, compiler.getRegister1())); // TODO Change to get DAddr
            compiler.addInstruction(new WFLOAT());
        } else {
            throw new DecacInternalError("Type of expression is not int or float");
        }

    }

    protected DVal getDVal(DecacCompiler compiler) {
        return dval;
    }

    protected boolean isImmediate() {
        return false;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    protected abstract void codeGenBool(DecacCompiler compiler, Label breakLabel, boolean branchOn);

}
