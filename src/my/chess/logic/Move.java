package my.chess.logic;

import static my.chess.logic.Piece.Type.KING;
import static my.chess.logic.Piece.Type.KNIGHT;

public class Move {

    private final V2<Integer> from, to;
    private final Player player;
    private final Piece piece;
    private Board board;

    private boolean isCheckmate, isCheck, isLegal, isCapturingMove, isPromotion, isCastling;

    public Move(V2<Integer> from, V2<Integer> to, Player player, Piece piece, Board board) {
        this.from = from;
        this.to = to;
        this.player = player;
        this.piece = piece;
        this.board = board;
    }

    private boolean validForPlayer() {
        return this.player.getColour() == this.piece.getColour();
    }

    private boolean validForPiece() {

        boolean isValid = true;

        if (!(piece.getType() == KING || piece.getType() == KNIGHT)) {
            isValid &= isUnobstructed();
        }

        isValid &= piece.isValidDestination(to);

        return isDifferentPosition() && isValid;
    }

    public boolean isValid() {
        return validForPlayer() && validForPiece();
    }

    // TODO: Implement
    public boolean isLegal() {
        return true;
    }

    // TODO: Implement
    public boolean isCheckmate() {
        return false;
    }

    public boolean isCapturingMove() {
        Piece destPiece = board.getPieceAt(to);
        return destPiece != null && (player.getColour() != destPiece.getColour());
    }

    private boolean isDifferentPosition() {
        return !from.equals(to);
    }

    private boolean isUnobstructed() {
        boolean isUnobstructed = true;
        int dx, dy, length;
        Piece destPiece;
        V2<Integer> point;

        length = ((to.x - from.x) != 0) ? Math.abs(to.x - from.x) : Math.abs(to.y - from.y);

        for (int i=1; i <= length; i++) {

            if (!from.x.equals(to.x)) {
                dx = (to.x > from.x) ? i : -i;
            } else {
                dx = 0;
            }

            if (!from.y.equals(to.y)) {
                dy = (to.y > from.y) ? i : -i;
            } else {
                dy = 0;
            }

            point = new V2<>(from.x+dx, from.y+dy);
            destPiece = board.getPieceAt(point);

            // Special case for capturing move (destination piece contains enemy piece, therefore not obstructed)
            if (destPiece != null) {
                if (!point.equals(to)) {
                    isUnobstructed &= board.getPieceAt(new V2<>(from.x+dx, from.y+dy)) == null;
                } else {
                    isUnobstructed &= destPiece.getColour() != this.piece.getColour();
                }
            }
        }

        return isUnobstructed;
    }

    // TODO: Implement
    public String toACN() {
        return "";
    }

//    private boolean piece

//    private boolean notUnderCheck
}
