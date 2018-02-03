package my.chess.logic;

import static my.chess.logic.Piece.Type.ROOK;

public final class Rook extends Piece {

    public Rook(Colour colour, V2<Integer> position) {
        super(ROOK, colour, position);
    }

    @Override
    public boolean isValidDestination(V2<Integer> destination) {
        // Need to check for path obstruction separately
        V2<Integer> currentPosition = this.getPosition();

        boolean horizontal = (!destination.x.equals(currentPosition.x)) && (destination.y.equals(currentPosition.y));
        boolean vertical = (!destination.y.equals(currentPosition.y)) && (destination.x.equals(currentPosition.x));

        return horizontal ^ vertical;
    }
}
