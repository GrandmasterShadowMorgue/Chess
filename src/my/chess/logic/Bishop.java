package my.chess.logic;

import static my.chess.logic.Piece.Type.BISHOP;

public final class Bishop extends Piece {

    public Bishop(Colour colour, V2<Integer> position) {
        super(BISHOP, colour, position);
    }

    @Override
    public boolean isValidDestination(V2<Integer> destination) {
        // Need to check for path obstruction separately
        V2<Integer> currentPosition = this.getPosition();
        boolean isDiagonal, isDifferentPosition;

        isDifferentPosition = !destination.equals(currentPosition);
        isDiagonal = Math.abs(destination.x - currentPosition.x) == Math.abs(destination.y - currentPosition.y);

        return isDifferentPosition && isDiagonal;
    }
}
