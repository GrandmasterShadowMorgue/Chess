package my.chess.logic;

import java.util.ArrayList;
import java.util.List;

// A Game is a record of moves, can be used to
// save and load game state. (Uses algebraic chess notation)
public class Game {

    // TODO: Turn counter display (length of list)
    List<Move> moves;

    public Game() {
        this.moves = new ArrayList<>();
    }

    public void update(Move move) {
        this.moves.add(move);
    }

    public Move getLastMove() {
        return this.moves.get(this.moves.size()-1);
    }
}
