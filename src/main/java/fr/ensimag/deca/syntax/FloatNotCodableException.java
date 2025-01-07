package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.Token;

public class FloatNotCodableException extends DecaRecognitionException {
    private final String name;
    private final Token offendingToken;

    public FloatNotCodableException(DecaParser recognizer, Token offendingToken) {
        super(recognizer, offendingToken);
        this.name = offendingToken.getText();
        this.offendingToken = offendingToken;
    }

    @Override
    public String getMessage() {
        float floatTokenValue = Float.parseFloat(offendingToken.getText());
        if (Float.isInfinite(floatTokenValue))
            return this.name + " is to big and it is interpreted as infinite";
        else if (Float.isNaN(floatTokenValue))
            return this.name + " is to small and it is interpreted to NaN ";
        else
            return "Erreur when parsing " + this.name;
    }
}
