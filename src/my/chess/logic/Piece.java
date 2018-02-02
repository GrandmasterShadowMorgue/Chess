package my.chess.logic;

public class Piece {

    private Type type;
    private Colour colour;

    public Piece(Type type, Colour colour) {
        this.type = type;
        this.colour = colour;
    }

    public Type getType() {
        return type;
    }

    public Colour getColour() {
        return colour;
    }

    enum Type {
        BISHOP,
        KING,
        KNIGHT,
        PAWN,
        QUEEN,
        ROOK
    }
}
