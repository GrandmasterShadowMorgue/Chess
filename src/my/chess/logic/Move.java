package my.chess.logic;

import static my.chess.logic.Piece.Type.KING;
import static my.chess.logic.Piece.Type.KNIGHT;
import static my.chess.logic.Piece.Type.PAWN;

public class Move {

    public V2<Integer> getFrom() {
        return from;
    }

    public V2<Integer> getTo() {
        return to;
    }

    public Player getPlayer() {
        return player;
    }

    public Piece getPiece() {
        return piece;
    }

    private final V2<Integer> from, to;
    private final Player player;
    private final Piece piece;

    private boolean isCheckmate;

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    private boolean isCheck;

    public boolean isCapturingMove() {
        return isCapturingMove;
    }

    public void setCapturingMove(boolean capturingMove) {
        isCapturingMove = capturingMove;
    }

    private boolean isCapturingMove;

    public Move(V2<Integer> from, V2<Integer> to, Player player, Piece piece) {
        this.from = from;
        this.to = to;
        this.player = player;
        this.piece = piece;
    }

    private boolean validForPlayer() {
        return this.player.getColour() == this.piece.getColour();
    }

    private boolean validForPiece() {

        boolean isValid = true;

        if (this.isCapturingMove && this.piece.getType() == PAWN) {
            isValid = ((Pawn) piece).isValidCapturingMove(to);
        } else {
            isValid &= piece.isValidDestination(to);
        }

        return isDifferentPosition() && isValid;
    }

    public boolean isValid() {
        return validForPlayer() && validForPiece();
    }

    private boolean isDifferentPosition() {
        return !from.equals(to);
    }

    // TODO: Implement
    public String toACN() {
        return "";
    }
}
