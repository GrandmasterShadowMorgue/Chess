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

        String emptyTile = (System.getProperty("os.name").contains("Windows")) ? "\u2002 \u2002" : "  ";

        String tile = "";
        StringBuilder row = new StringBuilder();

        Piece piece;
        String pieceSymbol;

        for (int i = 0; i < board.LENGTH; i++) {

            row.append(String.format("%d ", board.LENGTH - i));

            tile = (i % 2 == 0) ? whiteTile : blackTile;

            for (int j = 0; j < board.WIDTH; j++) {

                piece = board.getPieceAt(new V2<>(j,i));
                pieceSymbol = (piece == null) ? emptyTile : getUnicodeSymbol(piece);

                row.append(tile).append(pieceSymbol);

                tile = tile.equals(whiteTile) ? blackTile : whiteTile;
            }

            row.append(blackTile).append('\n');
        }

        row.append(" ");


        for (char i = 'A'; i < 'I'; i++) {
            row.append(emptyTile + i);
        }

        System.out.println(row);
    }

    private String getUnicodeSymbol(Piece piece) {
        switch (piece.colour) {
            case BLACK:
                switch (piece.type) {
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
                switch (piece.type) {
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
