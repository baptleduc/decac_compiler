package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.Token;

/**
 * Exception raised by a parser when an int can not be represented with 32 bits
 *
 */
public class IntNotCodableException extends DecaRecognitionException {
    private static final long serialVersionUID = -3517868082633812254L;
    private final String name;

    public IntNotCodableException(DecaParser recognizer, Token offendingToken) {
        super(recognizer, offendingToken);
        this.name = offendingToken.getText();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return name + " : can not be represented with 32 bits ";
    }
}
