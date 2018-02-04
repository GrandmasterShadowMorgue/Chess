package my.chess;

import my.chess.input.CastlingInputException;
import my.chess.input.InputManager;
import my.chess.input.InvalidInputException;
import my.chess.logic.*;
import my.chess.view.Renderer;

import java.util.Scanner;

import static my.chess.logic.Colour.*;

public class Controller {

    private Board board;
    private Renderer renderer;
    private InputManager inputManager;

    private Player whitePlayer, blackPlayer, currentPlayer;

    private Scanner scanner;

    private boolean isGameOver;

    public Controller() {

        this.board = new Board();
        this.renderer = new Renderer(board);
        this.inputManager = new InputManager();
        this.whitePlayer = new Player(WHITE);
        this.blackPlayer = new Player(BLACK);
        this.currentPlayer = whitePlayer;
        this.isGameOver = false;
        scanner = new Scanner(System.in);
    }

    public void runGame() {

        printWelcomeMessage();
        Move move;

        while (!isGameOver) {

            System.out.println(currentPlayer.getColour().toString() + " to move.\n");

            renderer.update();

            move = promptInput();

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
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (CastlingInputException e) {
                System.out.println(e.getMessage());
                continue;
            }

            Move move = new Move(rawMove.x, rawMove.y, currentPlayer, this.board.getPieceAt(rawMove.x), board);

            if (move.isValid() && move.isLegal()) {
                board.movePiece(rawMove.x, rawMove.y);
            }

            return move;
        }
    }

    public boolean promptRestart() {

        System.out.println("Start a new match? (Y/N):");

        String input;
        input = scanner.nextLine();

        while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"))) {

            System.out.println("Please enter either Y or N (Case insensitive):");
            input = scanner.nextLine();
        }

        return input.equalsIgnoreCase("Y");
    }
}
