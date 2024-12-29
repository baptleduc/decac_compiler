package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.IntStream;

/**
 * Exception raised by a parser when a int can not be represented as an integer with 32 bits
 *
 */
public class IntNotCodable extends DecaRecognitionException {
    private static final long serialVersionUID = -3517868082633812254L;
    private final String name;

    public IntNotCodable(DecaParser recognizer, Token offendingToken) {
        super(recognizer, offendingToken);
        this.name = offendingToken.getText();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return  name + " : can not be represented as an integer with 32 bits ";
    }
}
