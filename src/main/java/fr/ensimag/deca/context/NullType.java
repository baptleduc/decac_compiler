package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class NullType extends Type {

    public NullType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isNull();
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    @Override

    public boolean isSubClassOf(ClassType potentialSuperClass) {
        return true;
    }
}
