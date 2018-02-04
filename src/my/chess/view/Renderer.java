package my.chess.view;

import my.chess.logic.Board;
import my.chess.logic.Piece;
import my.chess.logic.V2;

public class Renderer {

    private Board board;

    public Renderer(Board board) {
        this.board = board;
    }

    public void update() {
        String whiteTile = "\033[0;377m ";
        String blackTile = "\033[40m ";

        String tile = "";
        StringBuilder row = new StringBuilder();

        Piece piece;
        String pieceSymbol;

        for (int i = 0; i < board.getLENGTH(); i++) {

            row.append(String.format("%d ", board.getLENGTH() - i));

            tile = (i % 2 == 0) ? whiteTile : blackTile;

            for (int j = 0; j < board.getWIDTH(); j++) {

                piece = board.getPieceAt(new V2<>(j,i));
                pieceSymbol = (piece == null) ? "\u2002 \u2002" : getUnicodeSymbol(piece);

                row.append(tile).append(pieceSymbol);

                tile = tile.equals(whiteTile) ? blackTile : whiteTile;
            }

            row.append(blackTile).append('\n');
        }

        row.append(" \u2002\u2002 A\u2002\u2002 B\u2002\u2002 C\u2002\u2002 D\u2002\u2002 E\u2002\u2002 F\u2002\u2002 G\u2002\u2002 H");

        System.out.println(row);
    }

    private String getUnicodeSymbol(Piece piece) {
        switch (piece.getColour()) {
            case BLACK:
                switch (piece.getType()) {
                    case BISHOP:
                        return "\u2657 ";
                    case KING:
                        return "\u2654 ";
                    case KNIGHT:
                        return "\u2658 ";
                    case PAWN:
                        return "\u2659 ";
                    case QUEEN:
                        return "\u2655 ";
                    case ROOK:
                        return "\u2656 ";
                }
                break;
            case WHITE:
                switch (piece.getType()) {
                    case BISHOP:
                        return "\u265D ";
                    case KING:
                        return "\u265A ";
                    case KNIGHT:
                        return "\u265E ";
                    case PAWN:
                        return "\u265F ";
                    case QUEEN:
                        return "\u265B ";
                    case ROOK:
                        return "\u265C ";
                }
                break;
        }

        return "";
    }

}
