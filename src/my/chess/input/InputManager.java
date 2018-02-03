package my.chess.input;

import my.chess.logic.V2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager {

    public V2<V2<Integer>> parseInput(String rawInput) throws InvalidInputException {
        Pattern p = Pattern.compile("([a-hA-H][1-8]) ([a-hA-H][1-8])");
        Matcher m = p.matcher(rawInput);

        String from = m.group(1).toLowerCase();
        String to = m.group(2).toLowerCase();

        if ((from != null && !from.isEmpty()) && (to != null && !to.isEmpty())) {
            return new V2<>(toPoint(from), toPoint(to));
        } else {
            throw new InvalidInputException("Invalid input. Input should be in the form: \"[a-h][1-8] [a-h[1-8]\" (Case insensitive)");
        }
    }

    private V2<Integer> toPoint(String tile) {
        return new V2<>(tile.charAt(0) - 97, tile.charAt(0) - 49);
    }
}
