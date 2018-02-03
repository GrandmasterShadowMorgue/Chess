package my.chess.input;

public class CastlingInputException extends Exception {

    public CastlingInputException(String message) {
        super(message);
    }

    public CastlingInputException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
