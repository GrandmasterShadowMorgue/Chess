package my.chess;

import my.chess.input.InputManager;
import my.chess.logic.*;
import my.chess.view.Renderer;

import java.util.Scanner;

import static my.chess.logic.Colour.*;

public class Controller {

    private Board board;
    private Renderer renderer;
    private InputManager inputManager;

    private Player whitePlayer, blackPlayer;

    private Scanner scanner;

    public Controller() {

        this.board = new Board();
        this.renderer = new Renderer(board);
        this.inputManager = new InputManager();
        this.whitePlayer = new Player(WHITE);
        this.blackPlayer = new Player(BLACK);

        scanner = new Scanner(System.in);
    }

    public void runGame() {

    }

    public boolean promptRestart() {

        System.out.println("Restart game? (Y/N):");

        String input;
        input = scanner.nextLine();

        while (!(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"))) {

            System.out.println("Please enter either Y or N (Case insensitive):");
            input = scanner.nextLine();
        }

        return input.equalsIgnoreCase("Y");
    }
}
