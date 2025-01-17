package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

/**
 * Representation of a label in IMA code. The same structure is used for label
 * declaration (e.g. foo: instruction) or use (e.g. BRA foo).
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class Label extends Operand {

    private static int counter = 0;

    @Override
    public String toString() {
        return name;
    }

    /**
     * Constructor with a counter appended to the name.
     * 
     * @param baseName
     *            The base name of the label
     */
    public Label(String baseName) {
        this(baseName, true); // Default to appending the counter
    }

    /**
     * Constructor with an option to include a counter or not.
     * 
     * @param baseName
     *            The base name of the label
     * @param includeCounter
     *            If true, append a unique counter to the label name
     */
    public Label(String baseName, boolean includeCounter) {
        super();
        String finalName = includeCounter ? baseName + "_" + counter++ : baseName;

        Validate.isTrue(finalName.length() <= 1024, "Label name too long, not supported by IMA");
        Validate.isTrue(finalName.matches("^[a-zA-Z][a-zA-Z0-9_.]*$"), "Invalid label name " + finalName);

        this.name = finalName;
    }

    private String name;
}
