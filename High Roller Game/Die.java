import java.util.Random;

/**
 * A simple class for representing die objects. A die has a given number of
 * sides (at least) four, set when the die is constructed and which can never
 * be changed. The die's value can be changed, but only by calling its roll()
 * method.
 */
public class Die {
    private static Random random = new Random();
    public static String SIX_SIDED_DIE_EMOJI = "🎲";
    private int currentValue;
    private int sides;

    /**
     * Constructs a die with the given starting value and number of sides.
     *
     * @throws IllegalArgumentException if the number of sides is less than 4 or
     *     if the starting value is not consistent with the number of sides.
     */
    public Die(int initialValue, int tempSides) {
        if (initialValue > tempSides || initialValue < 1) {
            throw new IllegalArgumentException("Die value not legal for die shape");
        } else if (tempSides < 4) {
            throw new IllegalArgumentException("At least four sides required");
        }
        currentValue = initialValue;
        sides = tempSides;
    }

    /**
     * Simulates a roll by randomly updating the value of this die. In addition to
     * mutating the die's value, this method also returns the new updated value.
     */
    public int roll() {
        currentValue = random.nextInt(sides) + 1;
        return currentValue;
    }

    /**
     * Returns the number of sides of this die.
     */
    public int getNumberOfSides() {
        return sides;
    }

    /**
     * Returns the current value of this die.
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * Returns a description of this die, which is its value enclosed in square
     * brackets, without spaces, for example "[5]".
     */
    @Override public String toString() {
        return "[" + currentValue + "]";
    }
}
