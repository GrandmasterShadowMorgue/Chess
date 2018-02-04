package my.chess;

import my.chess.logic.Board;
import my.chess.view.Renderer;

public class Main {

    public static void main(String[] args) {
        // write your code here

//        Board board = new Board();
//        Renderer renderer = new Renderer(board);
//        System.out.println("\033[47m \u2654 \033[40m");
//        renderer.update();
        Controller controller= new Controller();

        controller.runGame();
    }
}
