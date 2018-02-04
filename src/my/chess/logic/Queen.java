package my.chess.logic;

import static my.chess.logic.Piece.Type.QUEEN;

public final class Queen extends Piece {

    public Queen(Colour colour, V2<Integer> position) {
        super(QUEEN, colour, position);
    }

    @Override
    public boolean isValidDestination(V2<Integer> destination) {
        // Need to check for path obstruction separately
        V2<Integer> currentPosition = this.getPosition();
        boolean isDiagonal;

        isDiagonal = Math.abs(destination.x - currentPosition.x) == Math.abs(destination.y - currentPosition.y);

        boolean isBishopMove = isDiagonal;

        boolean horizontal = (!destination.x.equals(currentPosition.x)) && (destination.y.equals(currentPosition.y));
        boolean vertical = (!destination.y.equals(currentPosition.y)) && (destination.x.equals(currentPosition.x));

        boolean isRookMove = horizontal ^ vertical;

        return isBishopMove || isRookMove;
    }
}
