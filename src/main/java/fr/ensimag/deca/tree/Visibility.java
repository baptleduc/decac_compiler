package fr.ensimag.deca.tree;

/**
 * Visibility of a field.
 *
 * @author gl12
 * @date 01/01/2025
 */

public enum Visibility {
    PUBLIC("public"), PROTECTED("protected"), PRIVATE("private");

    private String name;
    
    @Override
    public String toString() {
        return name;
    }

    private Visibility(String name) {
        this.name = name;
    }
}
