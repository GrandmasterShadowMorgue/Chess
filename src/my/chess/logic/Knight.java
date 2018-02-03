package my.chess.logic;

import static my.chess.logic.Piece.Type.KNIGHT;

public final class Knight extends Piece {

    public Knight(Colour colour, V2<Integer> position) {
        super(KNIGHT, colour, position);
    }

    @Override
    public boolean isValidDestination(V2<Integer> destination) {
        V2<Integer> currentPosition = this.getPosition();
        boolean isHorizontalL, isVerticalL;

        isHorizontalL = (Math.abs(currentPosition.x - destination.x) == 2) && (Math.abs(currentPosition.y - destination.y) == 1);
        isVerticalL = (Math.abs(currentPosition.y - destination.y) == 2) && (Math.abs(currentPosition.x - destination.x) == 1);

        return isHorizontalL || isVerticalL;
    }
}
