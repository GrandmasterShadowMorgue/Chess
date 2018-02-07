package my.chess.logic;

public abstract class Piece {

    public final Type type;
    public final Colour colour;
    private V2<Integer> position;

    public Piece(Type type, Colour colour, V2<Integer> position) {
        this.type = type;
        this.colour = colour;
        this.position = position;
    }

    public V2<Integer> getPosition() {
        return position;
    }

    public void setPosition(V2<Integer> position) {
        this.position = position;
    }

    public abstract boolean isValidDestination(V2<Integer> destination);

    public enum Type {
        BISHOP,
        KING,
        KNIGHT,
        PAWN,
        QUEEN,
        ROOK
    }
}
