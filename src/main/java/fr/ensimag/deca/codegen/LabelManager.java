package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.ima.pseudocode.Label;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for predefined labels used in the Deca program.
 */
public enum LabelManager {

    // Error labels
    STACK_OVERFLOW_ERROR("stack_overflow_error"), IO_ERROR("io_error"), OVERFLOW_ERROR(
            "overflow_error"), DIVIDE_BY_ZERO_ERROR("divide_by_zero_error");

    private final Label label;

    // Map to store dynamically created labels
    private static final Map<String, Label> initLabels = new HashMap<>();
    private static final Map<String, Label> methodLabels = new HashMap<>();

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

    public static Label getInitLabel(AbstractIdentifier ident) {
        String labelName = "init." + ident.getName().getName();
        if (initLabels.containsKey(labelName)) {
            return initLabels.get(labelName);
        }
        return new Label(labelName, false);
    }
}
