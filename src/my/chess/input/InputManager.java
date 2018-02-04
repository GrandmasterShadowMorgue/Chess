package my.chess.input;

import my.chess.logic.V2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager {

    public InputManager() {
    }

    public V2<V2<Integer>> parseInput(String rawInput) throws InvalidInputException, CastlingInputException {

        if (rawInput.equals("0-0") || rawInput.equals("0-0-0")) {
            throw new CastlingInputException("Castling is currently not supported.");
        }

        Pattern p = Pattern.compile("([a-hA-H][1-8]) ([a-hA-H][1-8])");
        Matcher m = p.matcher(rawInput);

        m.find();

        String from = null;
        String to = null;
        try {
            from = m.group(1).toLowerCase();
            to = m.group(2).toLowerCase();
        } catch (Exception e) {
            throw new InvalidInputException("Invalid input. Input should be in the form: \"[a-h][1-8] [a-h[1-8]\" (Case insensitive)");
        }

        if ((from != null && !from.isEmpty()) && (to != null && !to.isEmpty())) {
            return new V2<>(toPoint(from), toPoint(to));
        } else {
            throw new InvalidInputException("Invalid input. Input should be in the form: \"[a-h][1-8] [a-h[1-8]\" (Case insensitive)");
        }
    }

    private V2<Integer> toPoint(String tile) {
        return new V2<>(tile.charAt(0) - 97, 7 - (tile.charAt(1) - 49));
    }
}
