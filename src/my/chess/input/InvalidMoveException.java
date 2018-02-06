package my.chess.input;

public class InvalidMoveException  extends Exception {

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
