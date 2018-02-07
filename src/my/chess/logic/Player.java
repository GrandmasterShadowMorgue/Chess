package my.chess.logic;

public class Player {

    public final Colour colour;
    private int points;

    public Player(Colour colour) {
        this.colour = colour;
        this.points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
