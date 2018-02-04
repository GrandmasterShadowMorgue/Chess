package my.chess.logic;

import static my.chess.logic.Colour.*;
import static my.chess.logic.Piece.Type.*;


public class Board {

    private final int LENGTH = 8;
    private final int WIDTH = 8;

    private Piece[][] tiles;

    public int getLENGTH() {
        return LENGTH;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public Board() {

        this.tiles = new Piece[LENGTH][WIDTH];
        initialiseTiles();
    }

    public void movePiece(V2<Integer> from, V2<Integer> to) {
        this.tiles[to.y][to.x] = this.tiles[from.y][from.x];
        this.tiles[from.y][from.x] = null;
    }

    public Piece getPieceAt(V2<Integer> point) {
        return this.tiles[point.y][point.x];
    }

    private void initialiseTiles() {
        addPawns(1);
        addPawns(6);
        addPieces(0);
        addPieces(7);
    }

    private void addPawns(int row) {
        for (int i = 0; i < WIDTH; i++) {
            this.tiles[row][i] = new Pawn(row == 1 ? BLACK : WHITE, new V2<>(i,row));
        }
    }

    private void addPieces(int row) {
        for (int i = 0; i < WIDTH/2; i++) {
            if (i == 0) {
                this.tiles[row][i] = new Rook(row == 0 ? BLACK : WHITE, new V2<>(i,row));
                this.tiles[row][WIDTH-i-1] = new Rook(row == 0 ? BLACK : WHITE, new V2<>(WIDTH-i-1, row));
            } else if (i == 1) {
                this.tiles[row][i] = new Knight(row == 0 ? BLACK : WHITE, new V2<>(i, row));
                this.tiles[row][WIDTH-i-1] = new Knight(row == 0 ? BLACK : WHITE, new V2<>(WIDTH-i-1, row));
            } else if (i == 2) {
                this.tiles[row][i] = new Bishop(row == 0 ? BLACK : WHITE, new V2<>(i, row));
                this.tiles[row][WIDTH-i-1] = new Bishop(row == 0 ? BLACK : WHITE, new V2<>(WIDTH-i-1, row));
            } else {
                this.tiles[row][i] = new Queen(row == 0 ? BLACK : WHITE, new V2<>(i, row));
                this.tiles[row][i+1] = new King(row == 0 ? BLACK : WHITE, new V2<>(i+1, row));
            }
        }
    }
}
