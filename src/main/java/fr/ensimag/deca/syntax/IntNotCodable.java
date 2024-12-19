package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;

/**
 * Exception raised when a int can not be represented as an integer with 32 bits
 *
 */
public class IntNotCodable extends DecaRecognitionException {
    private static final long serialVersionUID = -3517868082633812254L;
    private final String name;

    public IntNotCodable(String name, AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return  name + " : can not be represented as an integer with 32 bits ";
    }
}
