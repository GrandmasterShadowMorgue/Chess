package my.chess;

import my.chess.input.InputManager;
import my.chess.input.InvalidInputException;
import my.chess.input.InvalidMoveException;
import my.chess.logic.*;
import my.chess.view.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static my.chess.logic.Colour.*;

public class Controller {

    private Board board;
    private Renderer renderer;
    private InputManager inputManager;

    private Player whitePlayer, blackPlayer, currentPlayer;

    private Scanner scanner;

    private boolean isGameOver;

    private Game game;

    public Controller() {

        this.board = new Board();
        this.renderer = new Renderer(board);
        this.inputManager = new InputManager();
        this.whitePlayer = new Player(WHITE);
        this.blackPlayer = new Player(BLACK);
        this.currentPlayer = whitePlayer;
        this.isGameOver = false;
        this.scanner = new Scanner(System.in);
        this.game = new Game();
    }

    public void runGame() {

        printWelcomeMessage();
        Move move;

        while (!isGameOver) {

            System.out.println(currentPlayer.getColour().toString() + " to move.\n");

            renderer.update();

            move = promptInput();

            this.game.update(move);

            isGameOver = move.isCheckmate(); // or stalemate / draw rules

            currentPlayer = (currentPlayer.getColour() == WHITE) ? blackPlayer : whitePlayer;
        }

        promptRestart();
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to Chess! Let the game begin:");
    }

    public Move promptInput() {

        while (true) {

            System.out.println("\nEnter your move:");

            String rawInput = scanner.nextLine();
            V2<V2<Integer>> rawMove;

            try {
                rawMove = inputManager.parseInput(rawInput);

                Piece piece = this.board.getPieceAt(rawMove.x);
                if (piece == null) {
                    System.out.println("Empty square selected for movement.");
                    continue;
                }

                Move move = new Move(rawMove.x, rawMove.y, currentPlayer, piece);

                validateMove(move);

                return move;
            } catch (InvalidInputException | InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void validateMove(Move move) throws InvalidMoveException {

        Piece piece = move.getPiece();

        boolean isValid = move.isValid();

        if (isCapturingMove(move)) {
            move.setCapturingMove(true);
        }

        if (piece.getType() != Piece.Type.KNIGHT && piece.getType() != Piece.Type.PAWN) {
            isValid &= isUnobstructed(move, this.board);
        } else {
            Piece destPiece = this.board.getPieceAt(move.getTo());
            if (destPiece != null) {
                isValid &= destPiece.getColour() != piece.getColour();
            }
        }

        if (isValid) {

            if (isLegal(move)) {
                if (isCheck(move)) {
//                    if (isCheckmate(move)) {
//                        move.setCheckmate(true);
//                        System.out.println("CHECKMATE!");
//                    } else {
                        move.setCheck(true);
                        System.out.println("CHECK!");
//                    }
                }
                board.movePiece(move.getFrom(), move.getTo());
            } else {
                throw new InvalidMoveException("Illegal move. Your king is under check.");
            }
        } else {
            throw new InvalidMoveException("Invalid move; does not follow movement rules for the piece.");
        }

    }

    public boolean promptRestart() {
        Move lastMove = this.game.getLastMove();
        if (lastMove.isCheckmate()) {
            String winMessage = lastMove.getPlayer().getColour() == WHITE ? "White wins!" : "Black wins";
            System.out.println(winMessage);
        }

        System.out.println("Start a new match? (Y/N):");

        String input;
        input = scanner.nextLine();

        while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"))) {

            System.out.println("Please enter either Y or N (Case insensitive):");
            input = scanner.nextLine();
        }

        return input.equalsIgnoreCase("Y");
    }

    private boolean isUnobstructed(Move move, Board boardy) {
        boolean isUnobstructed = true;
        int dx, dy, length;
        Piece destPiece;
        V2<Integer> point;

        length = ((move.getTo().x - move.getFrom().x) != 0) ? Math.abs(move.getTo().x - move.getFrom().x) : Math.abs(move.getTo().y - move.getFrom().y);

        for (int i=1; i <= length; i++) {

            if (!move.getFrom().x.equals(move.getTo().x)) {
                dx = (move.getTo().x > move.getFrom().x) ? i : -i;
            } else {
                dx = 0;
            }

            if (!move.getFrom().y.equals(move.getTo().y)) {
                dy = (move.getTo().y > move.getFrom().y) ? i : -i;
            } else {
                dy = 0;
            }

            point = new V2<>(move.getFrom().x+dx, move.getFrom().y+dy);
            destPiece = boardy.getPieceAt(point);

            // Special case for capturing move (destination piece contains enemy piece, therefore not obstructed)
            if (destPiece != null) {
                if (!point.equals(move.getTo())) {
                    isUnobstructed &= boardy.getPieceAt(new V2<>(move.getFrom().x+dx, move.getFrom().y+dy)) == null;
                } else {
                    isUnobstructed &= destPiece.getColour() != move.getPiece().getColour();
                }
            }
        }

        return isUnobstructed;
    }

    private boolean isLegal(Move move) {
        Board updatedBoard = new Board(this.board.getTiles());

        updatedBoard.movePiece(move.getFrom(), move.getTo());

        Player enemy = this.currentPlayer.getColour() == WHITE ? this.blackPlayer : this.whitePlayer;
        Colour oppositeColour = this.currentPlayer.getColour() == WHITE ? BLACK : WHITE;
        List<Piece> enemyPieces = getAllPieces(oppositeColour);

        V2<Integer> kingPosition = getKingPosition(this.currentPlayer.getColour());

        boolean isUnderCheck = false;

        for (Piece piece :
                enemyPieces) {
            Move possibleCapturingMove = new Move(piece.getPosition(), kingPosition, enemy, piece);
            possibleCapturingMove.setCapturingMove(true);
            boolean obstructed = true;
            if (piece.getType() != Piece.Type.KNIGHT && piece.getType() != Piece.Type.PAWN) {
                obstructed = isUnobstructed(possibleCapturingMove, updatedBoard);
            }
            isUnderCheck |= possibleCapturingMove.isValid() && obstructed;
        }

        return !isUnderCheck;
    }

    private boolean isCheck(Move move) {
        Board updatedBoard = new Board(this.board.getTiles());
        updatedBoard.movePiece(move.getFrom(), move.getTo());
        List<Piece> playerPieces = getAllPieces(this.currentPlayer.getColour());
        Colour oppositeColour = this.currentPlayer.getColour() == WHITE ? BLACK : WHITE;
        V2<Integer> kingPosition = getKingPosition(oppositeColour);

        boolean isCheck = false;

        for (Piece piece: playerPieces) {
            Move possibleCapturingMove = new Move(piece.getPosition(), kingPosition, move.getPlayer(), piece);
            possibleCapturingMove.setCapturingMove(true);
            boolean obstructed = true;
            if (piece.getType() != Piece.Type.KNIGHT && piece.getType() != Piece.Type.PAWN) {
                obstructed = isUnobstructed(possibleCapturingMove, updatedBoard);
            }
            isCheck |= possibleCapturingMove.isValid() && obstructed;
        }

        return isCheck;
    }

    // TODO: Finish implementation
//    private boolean isCheckmate(Move move) {
//        Board updatedBoard = new Board(this.board.getTiles());
//        updatedBoard.movePiece(move.getFrom(), move.getTo());
//        Player enemy = this.currentPlayer.getColour() == WHITE ? this.blackPlayer : this.whitePlayer;
//        Colour oppositeColour = this.currentPlayer.getColour() == WHITE ? BLACK : WHITE;
//        List<Piece> enemyPieces = getAllPieces(oppositeColour);
//
//        // Can capture attacking piece(s)?
//        // Can block attacking piece(s)?
//        // Can move king to safety?
//    }

    // TODO: Implement (if needed)
//    private List<Move> getAllPossibleMoves(List<Piece> pieces) {
//        for (Piece piece :
//                pieces) {
//
//        }
//    }

    private List<Piece> getAllPieces(Colour colour) {
        List<Piece> pieces = new ArrayList<>();
        Piece piece;
        for (int i=0; i < this.board.getLENGTH(); i++) {
            for (int j=0; j< this.board.getWIDTH(); j++) {
                piece = this.board.getPieceAt(new V2<>(i, j));
                if (piece != null && (piece.getColour() == colour)) {
                    pieces.add(piece);
                }
            }
        }

        return pieces;
    }

    private V2<Integer> getKingPosition(Colour colour) {
        Piece piece;
        for (int i=0; i < this.board.getLENGTH(); i++) {
            for (int j=0; j< this.board.getWIDTH(); j++) {
                piece = this.board.getPieceAt(new V2<>(i, j));
                if (piece != null && (piece.getType() == Piece.Type.KING) && (piece.getColour() == colour)) {
                    return piece.getPosition();
                }
            }
        }
        return null;
    }

    private boolean isCapturingMove(Move move) {
        Piece destPiece = board.getPieceAt(move.getTo());

        return destPiece != null && (move.getPlayer().getColour() != destPiece.getColour());
    }
}
