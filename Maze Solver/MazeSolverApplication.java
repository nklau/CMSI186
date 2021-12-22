import java.util.Scanner;
import javax.swing.JFrame;

/**
 * MazeSolverApplication is a demo application that builds a mouse maze, displays
 * it, then runs an algorithm for leading the maze's mouse to the cheese. Mouse
 * and cheese coordinates are provided by the user at the command line; the maze
 * to use is taken from standard input, and is thus best built through
 * redirection from a file.
 */
public class MazeSolverApplication {
    /**
     * Launches MazeSolverApplication. The maze to use depend on what is provided via
     * standard input; the next two arguments are expected to be the mouse
     * coordinates while the following two arguments should be the cheese
     * coordinates. Any other set of arguments results in a usage help message.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        if (args.length != 4) {
            displayUsageMessage();
            return;
        }

        try {
            // Parse the arguments
            int mouseRow = Integer.parseInt(args[0]);
            int mouseColumn = Integer.parseInt(args[1]);
            int cheeseRow = Integer.parseInt(args[2]);
            int cheeseColumn = Integer.parseInt(args[3]);


            String mazeString = "";
            // TODO: Get the maze description from standard input using Scanner
            while (input.hasNext()) {
                mazeString += input.nextLine() + " ";
            }
            
            // Create the maze.
            MouseAndCheeseMaze mouseAndCheeseMaze = new MouseAndCheeseMaze(
                    new Maze(mazeString),
                    mouseRow, mouseColumn,
                    cheeseRow, cheeseColumn);

            // Create the window for the maze solver, then open it.
            JFrame f = new JFrame("MazeSolverApplication");
            f.setContentPane(new MouseAndCheeseMazeView(mouseAndCheeseMaze));
            f.pack();
            f.setLocationByPlatform(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        } catch (NumberFormatException nfe) {
            System.err.println("All coordinates must be integers.");
            System.err.println();
            displayUsageMessage();
            return;
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println();
            displayUsageMessage();
            return;
        }
    }

    /**
     * Displays a usage message.
     */
    private static void displayUsageMessage() {
        System.out.println("Usage: java MazeSolverApplication <rat row> <rat column> <cheese row> <cheese column>");
        System.out.println();
        System.out.println("The program then reads the maze to use through standard input.  For maximum");
        System.out.println("convenience, provide this maze using redirection from a text file:");
        System.out.println();
        System.out.println("    java MazeSolverApplication 0 0 2 2 < mazes/3x3.maze.txt");
        System.out.println();
        System.out.println("All rows must range from 0 to the height of the given maze - 1.");
        System.out.println("All columns must range from 0 to the width of the given maze - 1.");
        System.out.println("Coordinates must also refer to open cells within the maze.");
    }
}
