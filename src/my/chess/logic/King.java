package my.chess.logic;

import static my.chess.logic.Piece.Type.KING;

public final class King extends Piece {

    public King(Colour colour, V2<Integer> position) {

        super(KING, colour, position);
    }

    @Override
    public boolean isValidDestination(V2<Integer> destination) {
        V2<Integer> currentPosition = this.getPosition();
        boolean isNextX, isNextY, isDifferentPosition;

        isNextX = Math.abs(destination.x - currentPosition.x) <= 1;
        isNextY = Math.abs(destination.y - currentPosition.y) <= 1;
        isDifferentPosition = !destination.equals(currentPosition);

        return isDifferentPosition && (isNextX || isNextY);
    }
}
