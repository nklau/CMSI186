import java.util.Scanner;

/**
 * The game runner.
 */
public class HighRollerGame {
    private static Scanner scanner = new Scanner(System.in);
    private static DiceSet diceSet;
    private static String input = "";
    private static int highest = 0;

    public static void main(String[] args) {
        System.out.println("Welcome 🎲🎲🎲🎲🎲");
        do {
            System.out.println();
            System.out.print("Enter a command (h for help): ");
            input = scanner.nextLine().toLowerCase();

            if (input.equals("h") || input.equals("help")) {
                printHelpMessage();
            } else if (input.length() > 3 && input.substring(0, 3).equals("use")) {
                try {
                    int num1 = Integer.parseInt(input.substring(4, 5));
                    int num2 = Integer.parseInt(input.substring(6));
                    diceSet = createDiceSet(num1, num2);
                    System.out.println("You are now using a " + diceSet.getDescriptor());
                    System.out.println(diceSet);
                } catch (StringIndexOutOfBoundsException e) {
                    System.err.println("To create a new dice set with 2 dice of 6 sides each, type \"use 6 2\"");
                } catch (NumberFormatException e) {
                    System.err.println("To create a new dice set with 2 dice of 6 sides each, type \"use 6 2\"");
                } catch (NullPointerException e) {
                    System.err.println("You do not currently have a dice set.");
                }
            } else if (input.equals("roll all")) {
                try {
                    diceSet.rollAll();
                    System.out.println(diceSet);
                    checkHighest();
                } catch (NullPointerException e) {
                    System.err.println("You do not currently have a dice set.");
                }
            } else if (input.length() > 4 && input.substring(0, 4).equals("roll")) {  
                try {
                    diceSet.rollDie(Integer.parseInt(input.substring(5)) - 1);
                    System.out.println(diceSet);
                    checkHighest();
                } catch (StringIndexOutOfBoundsException e) {
                    System.err.println("To roll the second die in the dice set, type \"roll 2\"");
                } catch (NumberFormatException e) {
                    System.err.println("To roll the second die in the dice set, type \"roll 2\"");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("You do not have that many dice in your dice set.");
                } catch (NullPointerException e) {
                    System.err.println("You do not currently have a dice set.");
                }
            } else if (input.equals("high") || input.equals("highest")) {
                System.out.println("Highest score so far is " + highest);
            } else {
                if (!(input.equals("quit")) && !(input.equals("q"))) {
                    System.out.println("I don't understand");
                }
            }

        } while (!(input.equals("q")) && !(input.equals("quit")));
        System.out.println("I'm glad you played today. You look great!");
    }

    /**
     * Prints a help message.
     */
    private static void printHelpMessage() {
        System.out.println("h or help       : Prints this message");
        System.out.println("q or quit       : Quits the program");
        System.out.println("use <s> <n>     : Get a new dice set with n dice of s sides each");
        System.out.println("roll all        : Roll all the dice in your current dice set");
        System.out.println("roll <i>        : Roll the ith die of your current dice set");
        System.out.println("high or highest : Prints the highest roll so far");
    }

    /** 
     * Creates a new dice set.
     */
    private static DiceSet createDiceSet(int sides, int numberOfDice) {
        DiceSet newDiceSet = new DiceSet(sides, numberOfDice);
        return newDiceSet;
    }

    /**
     * Checks if the current roll is higher than the set highest roll so far.
     */
    private static void checkHighest() {
        int total = diceSet.getTotal();
        if (total > highest) {
            highest = total;
        }
    }
}