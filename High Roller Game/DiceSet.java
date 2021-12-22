import java.util.ArrayList;
import java.util.List;

/**
 * A dice set holds a collection of Die objects. All of the die objects have
 * the same number of sides.
 */
public class DiceSet {
    private Die[] diceSet;
    private int sides;

    /**
     * Creates a DiceSet containing the given number of dice, each with the
     * given number of sides. All die values start off as 1. Throws an
     * IllegalArgumentException if either less than two dice were provided
     * or if it is asked to make dice with less than 4 sides.
     */
    public DiceSet(int sidesOnEachDie, int numberOfDice) {
        if (numberOfDice < 2) {
            throw new IllegalArgumentException("At least two dice required");
        } else if (sidesOnEachDie < 4) {
            throw new IllegalArgumentException("Dice must have at least four sides");
        }
        diceSet = new Die[numberOfDice];
        sides = sidesOnEachDie;
        for (int i = 0; i < diceSet.length; i++) {
            diceSet[i] = new Die(1, sidesOnEachDie);
        }
    }

    /**
     * Creates a DiceSet where each die has the given number of sides, with the
     * given values.
     */
    public DiceSet(int sidesOnEachDie, int... values) {
        if (values.length < 2) {
            throw new IllegalArgumentException("At least two dice required");
        }
        diceSet = new Die[values.length];
        sides = sidesOnEachDie;
        for (int i = 0; i < values.length; i++) {
            diceSet[i] = new Die(values[i], sidesOnEachDie);
        }
    }

    /**
     * Returns the descriptor of the dice set; for example "5d20" for a set with
     * five dice of 20 sides each; or "2d6" for a set of two six-sided dice.
     */
    public String getDescriptor() {
        return diceSet.length + "d" + sides;
    }

    /**
     * Returns the size of the dice set.
     */
    public int getLength() {
        return diceSet.length;
    }

    /**
     * Returns the number of sides on each die.
     */
    public int getSides() {
        return sides;
    }

    /**
     * Returns the total of the values of each die in the set.
     */
    public int getTotal() {
        int totalValue = 0;
        for (int i = 0; i < diceSet.length; i++) {
            totalValue += diceSet[i].getCurrentValue();
        }
        return totalValue;
    }

    /**
     * Rolls all the dice in the set.
     */
    public void rollAll() {
        for (int i = 0; i < diceSet.length; i++) {
            diceSet[i].roll();
        }
    }

    /**
     * Rolls the i-th die, updating its value.
     */
    public void rollDie(int i) {
        diceSet[i].roll();
    }

    /**
     * Returns the value of the i-th die.
     */
    public int getDie(int i) {
        return diceSet[i].getCurrentValue();
    }

    /**
     * Returns the values of each of the dice in a list.
     */
    public List<Integer> getCurrentValues() {
        List<Integer> currentValues = new ArrayList<>();
        for (int i = 0; i < diceSet.length; i++) {
            currentValues.add(diceSet[i].getCurrentValue());
        }
        return currentValues;
    }

    /**
     * Returns whether this dice set has the same distribution of values as
     * another dice set. The two dice sets must have the same number of dice
     * and the same number of sides per dice, and there must be the same
     * number of each value in each set.
     */
    public boolean matches(DiceSet toCompare) {
        if (toCompare.getLength() != diceSet.length) {
            return false;
        }
        if (toCompare.getSides() != diceSet[0].getNumberOfSides()) {
            return false;
        }
        List<Integer> toCompareValues = toCompare.getCurrentValues();
        List<Integer> diceSetValues = this.getCurrentValues();
        for (int i = 0; i < diceSet.length; i++) {
            Integer diceSetValue = diceSetValues.get(i);
            if (!(toCompareValues.remove(diceSetValue))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation in which each of the die strings are
     * joined without a separator, for example: "[2][5][2][3]".
     */
    @Override public String toString() {
        String toReturn = "";
        for (int i = 0; i < diceSet.length; i++) {
            toReturn += "[" + diceSet[i].getCurrentValue() + "]";
        }
        return toReturn;
    }
}
