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

            System.out.println(currentPlayer.colour.toString() + " to move.\n");

            renderer.update();

            move = promptInput();

            this.game.update(move);

            isGameOver = move.isCheckmate(); // or stalemate / draw rules

            currentPlayer = (currentPlayer.colour == WHITE) ? blackPlayer : whitePlayer;
        }

        renderer.update();

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

        if (isCapturingMove(move, this.board)) {
            move.setCapturingMove(true);
        }

        boolean isValid = move.isValid();

        if (piece.type != Piece.Type.KNIGHT && piece.type != Piece.Type.PAWN) {
            isValid &= isUnobstructed(move, this.board);
        } else {
            Piece destPiece = this.board.getPieceAt(move.getTo());
            if (destPiece != null) {
                isValid &= destPiece.colour != piece.colour;
            }
        }

        if (isValid) {

            if (isLegal(move, this.board, this.currentPlayer.colour)) {
                if (isCheck(move, this.board)) {
                    if (isCheckmate(move)) {
                        move.setCheckmate(true);
                        System.out.println("CHECK...");
                    } else {
                        move.setCheck(true);
                        System.out.println("CHECK!");
                    }
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
            System.out.println("and MATE!");
            String winMessage = lastMove.getPlayer().colour == WHITE ? "White wins!" : "Black wins";
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

    private boolean isUnobstructed(Move move, Board board) {
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
            destPiece = board.getPieceAt(point);

            // Special case for capturing move (destination piece contains enemy piece, therefore not obstructed)
            if (destPiece != null) {
                if (!point.equals(move.getTo())) {
                    isUnobstructed &= board.getPieceAt(new V2<>(move.getFrom().x+dx, move.getFrom().y+dy)) == null;
                } else {
                    isUnobstructed &= destPiece.colour != move.getPiece().colour;
                }
            }
        }

        return isUnobstructed;
    }

    private boolean isLegal(Move move, Board board, Colour playerColour) {
        Board updatedBoard = new Board(board.getTiles());

        updatedBoard.movePiece(move.getFrom(), move.getTo());

        Player enemy = playerColour == WHITE ? this.blackPlayer : this.whitePlayer;
        Colour oppositeColour = playerColour == WHITE ? BLACK : WHITE;
        List<Piece> enemyPieces = getAllPieces(oppositeColour, updatedBoard);

        V2<Integer> kingPosition = getKingPosition(playerColour, updatedBoard);

        boolean isUnderCheck = false;

        for (Piece piece :
                enemyPieces) {
            Move possibleCapturingMove = new Move(piece.getPosition(), kingPosition, enemy, piece);
            possibleCapturingMove.setCapturingMove(true);
            boolean obstructed = true;
            if (piece.type != Piece.Type.KNIGHT && piece.type != Piece.Type.PAWN) {
                obstructed = isUnobstructed(possibleCapturingMove, updatedBoard);
            } else {
                Piece destPiece = board.getPieceAt(move.getTo());
                if (destPiece != null) {
                    obstructed = destPiece.colour != piece.colour;
                }
            }
            isUnderCheck |= possibleCapturingMove.isValid() && obstructed;
        }

        return !isUnderCheck;
    }

    private boolean isCheck(Move move, Board board) {
        Board updatedBoard = new Board(board.getTiles());
        updatedBoard.movePiece(move.getFrom(), move.getTo());
        List<Piece> playerPieces = getAllPieces(this.currentPlayer.colour, updatedBoard);
        Colour oppositeColour = this.currentPlayer.colour == WHITE ? BLACK : WHITE;
        V2<Integer> kingPosition = getKingPosition(oppositeColour, updatedBoard);

        boolean isCheck = false;

        for (Piece piece: playerPieces) {
            Move possibleCapturingMove = new Move(piece.getPosition(), kingPosition, move.getPlayer(), piece);
            possibleCapturingMove.setCapturingMove(true);
            boolean obstructed = true;
            if (piece.type != Piece.Type.KNIGHT && piece.type != Piece.Type.PAWN) {
                obstructed = isUnobstructed(possibleCapturingMove, updatedBoard);
            } else {
                Piece destPiece = board.getPieceAt(move.getTo());
                if (destPiece != null) {
                    obstructed = destPiece.colour != piece.colour;
                }
            }
            isCheck |= possibleCapturingMove.isValid() && obstructed;
        }

        return isCheck;
    }

    private boolean isCheckmate(Move move) {
        Board updatedBoard = new Board(this.board.getTiles());
        updatedBoard.movePiece(move.getFrom(), move.getTo());
        Player enemy = this.currentPlayer.colour == WHITE ? this.blackPlayer : this.whitePlayer;
        Colour oppositeColour = this.currentPlayer.colour == WHITE ? BLACK : WHITE;
        V2<Integer> kingPosition = getKingPosition(oppositeColour, updatedBoard);
        List<Piece> enemyPieces = getAllPieces(oppositeColour, updatedBoard);
        enemyPieces.removeIf(p -> p.type == Piece.Type.KING);
        List<Piece> playerPieces = getAllPieces(this.currentPlayer.colour, updatedBoard);
        List<Piece> checkPieces = new ArrayList<>();

        for (Piece piece :
                playerPieces) {
            Move possibleCapturingMove = new Move(piece.getPosition(), kingPosition, this.currentPlayer, piece);
            possibleCapturingMove.setCapturingMove(true);
            boolean obstructed = true;
            if (piece.type != Piece.Type.KNIGHT && piece.type != Piece.Type.PAWN) {
                obstructed = isUnobstructed(possibleCapturingMove, updatedBoard);
            } else {
                Piece destPiece = updatedBoard.getPieceAt(move.getTo());
                if (destPiece != null) {
                    obstructed = destPiece.colour != piece.colour;
                }
            }

            if (possibleCapturingMove.isValid() && obstructed) {
                checkPieces.add(piece);
            }
        }

        // Can capture attacking piece?
        if (checkPieces.size() == 1) {
            Piece checkPiece = checkPieces.get(0);
            for (Piece piece :
                    enemyPieces) {
                Move possibleCapturingMove = new Move(piece.getPosition(), checkPiece.getPosition(), enemy, piece);
                possibleCapturingMove.setCapturingMove(true);
                boolean obstructed = true;
                if (piece.type != Piece.Type.KNIGHT && piece.type != Piece.Type.PAWN) {
                    obstructed = isUnobstructed(possibleCapturingMove, updatedBoard);
                } else {
                    Piece destPiece = updatedBoard.getPieceAt(move.getTo());
                    if (destPiece != null) {
                        obstructed = destPiece.colour != piece.colour;
                    }
                }
                if (possibleCapturingMove.isValid() && obstructed) return false;
            }

            // Can block attacking piece(s)?
            if (checkPiece.type != Piece.Type.KNIGHT && checkPiece.type != Piece.Type.PAWN){
                Move possibleBlockingMove;
                int dx, dy;

                int length = ((kingPosition.x - checkPiece.getPosition().x) != 0) ? Math.abs(kingPosition.x - checkPiece.getPosition().x) : Math.abs(kingPosition.y - checkPiece.getPosition().y);
                for (int i=1; i <= length; i++) {

                    if (!kingPosition.x.equals(checkPiece.getPosition().x)) {
                        dx = (checkPiece.getPosition().x < kingPosition.x) ? i : -i;
                    } else {
                        dx = 0;
                    }

                    if (!kingPosition.y.equals(checkPiece.getPosition().y)) {
                        dy = (checkPiece.getPosition().y < kingPosition.y) ? i : -i;
                    } else {
                        dy = 0;
                    }

                    V2<Integer> blockingDestination = new V2<>(checkPiece.getPosition().x+dx, checkPiece.getPosition().y+dy);

                    for (Piece piece :
                            enemyPieces) {
                        possibleBlockingMove = new Move(piece.getPosition(), blockingDestination, enemy, piece);
                        boolean obstructed = true;
                        if (piece.type != Piece.Type.KNIGHT && piece.type != Piece.Type.PAWN) {
                            obstructed = isUnobstructed(possibleBlockingMove, updatedBoard);
                        } else {
                            Piece destPiece = updatedBoard.getPieceAt(move.getTo());
                            if (destPiece != null) {
                                obstructed = destPiece.colour != piece.colour;
                            }
                        }
                        if (possibleBlockingMove.isValid() && obstructed) return false;
                    }
                }
            }
        }
        // Can move king to safety?
        Piece enemyKing = updatedBoard.getPieceAt(kingPosition);
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (!(dx == 0 && dy == 0)) {
                    V2<Integer> possibleDest = new V2<>(kingPosition.x + dx, kingPosition.y + dy);
                    if ((possibleDest.x > -1 && possibleDest.y > -1) && (possibleDest.x < updatedBoard.LENGTH && possibleDest.y < updatedBoard.WIDTH)) {
                        Move kingMove = new Move(kingPosition, possibleDest, enemy, enemyKing);
                        if (isUnobstructed(kingMove, updatedBoard) && isLegal(kingMove, updatedBoard, enemy.colour)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private List<Piece> getAllPieces(Colour colour, Board board) {
        List<Piece> pieces = new ArrayList<>();
        Piece piece;
        for (int i=0; i < board.LENGTH; i++) {
            for (int j=0; j< board.WIDTH; j++) {
                piece = board.getPieceAt(new V2<>(i, j));
                if (piece != null && (piece.colour == colour)) {
                    pieces.add(piece);
                }
            }
        }

        return pieces;
    }

    private V2<Integer> getKingPosition(Colour colour, Board board) {
        Piece piece;
        for (int i=0; i < board.LENGTH; i++) {
            for (int j=0; j< board.WIDTH; j++) {
                piece = board.getPieceAt(new V2<>(i, j));
                if (piece != null && (piece.type == Piece.Type.KING) && (piece.colour == colour)) {
                    return piece.getPosition();
                }
            }
        }
        return null;
    }

    private boolean isCapturingMove(Move move, Board board) {
        Piece destPiece = board.getPieceAt(move.getTo());

        return destPiece != null && (move.getPlayer().colour != destPiece.colour);
    }
}
