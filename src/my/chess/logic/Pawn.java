package my.chess.logic;

import static my.chess.logic.Colour.BLACK;
import static my.chess.logic.Piece.Type.PAWN;

public final class Pawn extends Piece {

    public Pawn(Colour colour, V2<Integer> position) {
        super(PAWN, colour, position);
    }

    @Override
    public boolean isValidDestination(V2<Integer> destination) {
        // Need to check for path obstruction separately
        // Special case for capturing move
        V2<Integer> currentPosition = this.getPosition();
        boolean isForward, isValidDouble, isNextSquare;

        isForward = destination.x == currentPosition.x;
        isValidDouble = Math.abs(destination.y - currentPosition.y) == 2;
        isNextSquare = Math.abs(destination.y - currentPosition.y) == 1;

        if (this.getColour() == BLACK) {
            isForward &= destination.y > currentPosition.y;
            isValidDouble &= currentPosition.y == 1;
        } else {
            isForward &= destination.y < currentPosition.y;
            isValidDouble &= currentPosition.y == 6;
        }

        return isForward && (isValidDouble || isNextSquare);
    }
}
