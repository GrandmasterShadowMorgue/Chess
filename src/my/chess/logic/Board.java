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

    public void movePiece(int fromX, int fromY, int toX, int toY) {
        this.tiles[toX][toY] = this.tiles[fromX][fromY];
        this.tiles[fromX][fromY] = null;
    }

    public Piece getPieceAt(int x, int y) {
        return this.tiles[x][y];
    }

    private void initialiseTiles() {
        addPawns(1);
        addPawns(6);
        addPieces(0);
        addPieces(7);
    }

    private void addPawns(int row) {
        for (int i = 0; i < WIDTH; i++) {
            this.tiles[row][i] = new Piece(PAWN, row == 1 ? BLACK : WHITE);
        }
    }

    private void addPieces(int row) {
        for (int i = 0; i < WIDTH/2; i++) {
            if (i == 0) {
                this.tiles[row][i] = new Piece(ROOK, row == 0 ? BLACK : WHITE);
                this.tiles[row][WIDTH-i-1] = new Piece(ROOK, row == 0 ? BLACK : WHITE);
            } else if (i == 1) {
                this.tiles[row][i] = new Piece(KNIGHT, row == 0 ? BLACK : WHITE);
                this.tiles[row][WIDTH-i-1] = new Piece(KNIGHT, row == 0 ? BLACK : WHITE);
            } else if (i == 2) {
                this.tiles[row][i] = new Piece(BISHOP,  row == 0 ? BLACK : WHITE);
                this.tiles[row][WIDTH-i-1] = new Piece(BISHOP, row == 0 ? BLACK : WHITE);
            } else {
                this.tiles[row][i] = new Piece(QUEEN, row == 0 ? BLACK : WHITE);
                this.tiles[row][i+1] = new Piece(KING, row == 0 ? BLACK : WHITE);
            }
        }
    }
}
