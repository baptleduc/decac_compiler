package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

/**
 * Enum for predefined labels used in the Deca program.
 */
public enum LabelManager {

    // Error labels
    STACK_OVERFLOW_ERROR("stack_overflow_error"), IO_ERROR("io_error"), OVERFLOW_ERROR(
            "overflow_error"), DIVIDE_BY_ZERO_ERROR("divide_by_zero_error"),

    // Object related labels
    OBJECT_EQUALS_LABEL("code.Object.equals");

    private final Label label;

    /**
     * Constructor for the enum values.
     *
     * @param labelName
     *            the name of the label
     */
    LabelManager(String labelName) {
        this.label = new Label(labelName);
    }

    /**
     * Retrieves the Label instance associated with the enum value.
     *
     * @return the Label instance
     */
    public Label getLabel() {
        return label;
    }
}
