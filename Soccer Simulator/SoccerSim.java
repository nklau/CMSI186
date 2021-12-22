import java.text.DecimalFormat;

/**
 * Filename: SoccerSim.java
 * Description: Soccer simulator
 * Author: Natalie Lau
 * Date: 16 February 2021
 */
public class SoccerSim {
    private double timeSlice = 1.0;
    private double height = 1000;
    private double width = 1000;
    private int numBalls;
    private static Ball[] soccerBalls;

    /**
     * Constructor to create a new soccer simulation with a field and soccer balls.
     * Are there zero arguments? Are there 4 args per ball? Can the args be converted to doubles?
     * Are the args within range? Are there customizations: One extra argument represents a custom
     * time slice. Two extra arguments represent a custom field size, width followed by height.
     * Three extra arguments represent a custom field size followed by a custom time slice.
     *
     * @param args String array of the arguments supplied to the program
     */
    public SoccerSim(String... args) throws NumberFormatException, IllegalArgumentException {
        if (args.length == 0) {
            throw new IllegalArgumentException("At least four arguments per ball required (x, y, dx, dy)");
        } else if (args.length < 4) {
            throw new IllegalArgumentException("Requires 4 args per ball");
        }
        numBalls = args.length / 4;
        if (args.length % 4 == 1) {
            timeSlice = Double.parseDouble(args[args.length - 1]);
        } else if (args.length % 4 == 2) {
            width = Double.parseDouble(args[args.length - 2]);
            height = Double.parseDouble(args[args.length - 1]);
        } else if (args.length % 4 == 3) {
            width = Double.parseDouble(args[args.length - 3]);
            height = Double.parseDouble(args[args.length - 2]);
            timeSlice = Double.parseDouble(args[args.length - 1]);
        }
        if (timeSlice <= 0) {
            throw new IllegalArgumentException("Timeslice must be positive");
        }
        if (width < 1) {
            throw new IllegalArgumentException("Field width must be positive");
        }
        if (height < 1) {
            throw new IllegalArgumentException("Field height must be positive");
        }
        soccerBalls = new Ball[numBalls];
        for (int i = 0; i < soccerBalls.length; i++) {
            int x = Integer.parseInt(args[i * 4]);
            int y = Integer.parseInt(args[i * 4 + 1]);
            if (Math.abs(x) > width / 2 || Math.abs(y) > height / 2) {
                throw new IllegalArgumentException("Ball cannot start out of bounds");
            }
            double[] location = {x, y};
            double[] velocity = {Double.parseDouble(args[i * 4 + 2]), Double.parseDouble(args[i * 4 + 3])};
            soccerBalls[i] = new Ball(location, velocity);
        }
    }

    /**
     * Moves the balls in the soccerBall array by 1 timeslice.
     * Set ball out of bounds depending on the new location.
     */
    public void simUpdate() {
        for (Ball ball : soccerBalls) {
            ball.move(timeSlice);
            ball.checkBallOutOfBounds(width, height);
        }
    }

    /**
     * Checks if any balls collided with each other.
     * If so, the simulation is over.
     *
     * @return int array of ballsCollided
     */
    public int[] collisionCheck() {
        for (int i = 0; i < soccerBalls.length - 1; i++) {
            for (int j = i + 1; j < soccerBalls.length; j++) {
                double[] loc1 = soccerBalls[i].getCurrentLocation();
                double[] loc2 = soccerBalls[j].getCurrentLocation();
                double distance = Math.sqrt(Math.pow(loc1[0] - loc2[0], 2) + Math.pow(loc1[1] - loc2[1], 2));
                if (distance <= 8.9) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * Checks that at least one ball is still in bounds.
     */
    public boolean atLeastOneBallInBounds() {
        for (Ball ball : soccerBalls) {
            if (ball.isStillMoving()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether at least one ball is still moving.
     * The simulation ends when there's a collision, or all balls have stopped moving.
     *
     * @return boolean flag (true if at least one ball is moving, else false)
     */
    public boolean atLeastOneBallStillMoving() {
        for (Ball ball : soccerBalls) {
            if (ball.isStillMoving()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Runs the simulation.
     *
     * @return String result (if there's a collision, and how many iterations)
     */
    public static String runSimulation(String... args) {
        SoccerSim sim = new SoccerSim(args);
        sim.simUpdate();
        int[] collided = sim.collisionCheck();
        boolean run1 = true;
        int numIterations = 1;
        while (true) {
            sim.simUpdate();
            if (!run1) {
                collided = sim.collisionCheck();
            } else if (run1 && collided != null) {
                return ("Collision occurred between ball " + collided[0] + " and ball " + collided[1]
                        + ". Number of iterations: 1");
            }
            if (!sim.atLeastOneBallStillMoving()) {
                return ("NO COLLISION POSSIBLE! Number of iterations: " + (numIterations + 1));
            }
            if (collided != null) {
                return ("Collision occurred between ball " + collided[0] + " and ball " + collided[1]
                        + ". Number of iterations: " + (numIterations + 1));
            }
            numIterations++;
            run1 = false;
        }
    }

    /**
     * Main method of the simulation.
     *
     * @param  args  String array of the command line arguments
     */
    public static void main(String[] args) {
        String str = runSimulation(args);
        System.out.println(str);
    }
}
