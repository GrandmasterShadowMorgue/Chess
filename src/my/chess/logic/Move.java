package my.chess.logic;

import static my.chess.logic.Piece.Type.KING;
import static my.chess.logic.Piece.Type.KNIGHT;

public class Move {

    private final V2<Integer> from, to;
    private final Player player;
    private final Piece piece;
    private Board board;

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

        return isValid;
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

    // TODO: Implement
    private boolean isUnobstructed() {
        return true;
    }

    // TODO: Implement
    public String toACN() {
        return "";
    }

//    private boolean piece

//    private boolean notUnderCheck
}
