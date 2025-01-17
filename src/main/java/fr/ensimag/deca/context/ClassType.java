package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Type defined by a class.
 *
 * @author gl12
 * @date 01/01/2025
 */
public class ClassType extends Type {

    protected ClassDefinition definition;

    public ClassDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }

    @Override
    public boolean sameType(Type otherType) {
        if(!otherType.isClass()){
            return false;
        }
        else{
            try {
                ClassType otherClassType = otherType.asClassType("can't compare class type with a predef type", this.getDefinition().getLocation());
                if(otherClassType.getDefinition().equals(this.getDefinition())){
                    return true;
                }
            } catch (Exception e) {
                // do nothing
            }
            return false;
        }
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        ClassDefinition superClassDefinition = this.definition.getSuperClass();
        while(!superClassDefinition.getType().sameType(potentialSuperClass)
        && superClassDefinition.getSuperClass() != null){
            superClassDefinition = superClassDefinition.getSuperClass();
        }
        return superClassDefinition.getType().sameType(potentialSuperClass);

    }

}
