import java.text.DecimalFormat;

/**
 * Filename: Ball.java
 * Description: A soccer ball class
 * Author: Natalie Lau
 * Date: 11 February 2021
 */
public class Ball {
    /**
     * One percent slowdown per second due to friction.
     */
    private static final double FRICTION_COEFFICIENT = 0.99;

    /**
     * Radius in inches as given by the problem.
     */
    public static final double BALL_RADIUS = 4.45;

    /**
     * Helper constant to indicate the x component of location and velocity.
     */
    public static final int X_INDEX = 0;

    /**
     * Helper constant to indicate the y component of location and velocity.
     */
    public static final int Y_INDEX = 1;

    private boolean isInBounds = true; // all balls will start in bounds by default
    private double[] centerLocation;  // ball location (inches)
    private double[] currentVelocity; // in inches per second
    private double currentSpeed;

    /**
     * Constructor to make a new Ball.
     *
     * @param location location of the ball
     * @param velocity the initial velocity of the ball
     */
    public Ball(double[] location, double[] velocity) {
        centerLocation = location;
        currentVelocity = velocity;
        currentSpeed = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
    }

    /**
     * Returns the current velocity of the ball.
     */
    public double[] getCurrentVelocity() {
        return currentVelocity;
    }

    /**
     * Returns the current coordinates of the ball's center.
     */
    public double[] getCurrentLocation() {
        return centerLocation;
    }

    /**
     * Returns the ball's current speed, which is defined as the magnitude of the
     * current velocity vector.
     */
    public double getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * Determines if the ball is still moving.
     *
     * @return  boolean true if ball is moving, false if at rest
     *     Note: at rest is defined as speed <= 1.0
     */
    public boolean isStillMoving() {
        if (currentSpeed <= 1.0) {
            return false;
        }
        return true;
    }

    /**
     * Assuming a field of the given width and height, checks whether the ball would be
     * out of bounds at its current position. Use the official soccer definition of
     * this term: the _entire_ ball must be outside the field in order to be considered
     * as out of bounds.
     *
     * <p>The field's center is assumed to be the origin. Thus, its x coordinates range
     * from -fieldWidth / 2 to fieldWidth / 2 and its y coordinates range from
     * -fieldHeight / 2 to fieldHeight / 2.</p>
     *
     * <p>If the ball is out of bounds, this sets its velocity to (0.0, 0.0) and its
     * "isInBounds" value to false, effectively taking it out of the simulation.</p>
     *
     * @param fieldWidth    double-precision value of the designated field width
     * @param fieldHeight   double-precision value of the designated field height
     */
    public void checkBallOutOfBounds(double fieldWidth, double fieldHeight) {
        if (Math.abs(centerLocation[0]) - 4.45 > Math.abs(fieldWidth / 2)
                || Math.abs(centerLocation[1]) - 4.45 > Math.abs(fieldHeight / 2)) {
            currentVelocity = new double[]{0.0, 0.0};
            currentSpeed = 0;
            isInBounds = false;
        }
    }

    /**
     * Updates the velocity of the ball after the given timeslice, using FRICTION.
     *
     * @param timeSlice     double-precision value of the elapsed time, in seconds
     *
     * @return  double[] of new velocity, in inches per second
     */
    public double[] updateSpeedsForOneTick(double timeSlice) {
        currentVelocity[0] *= Math.pow(FRICTION_COEFFICIENT, timeSlice);
        currentVelocity[1] *= Math.pow(FRICTION_COEFFICIENT, timeSlice);
        currentSpeed = Math.sqrt(Math.pow(currentVelocity[0], 2) + Math.pow(currentVelocity[1], 2));
        return currentVelocity;
    }

    /**
     * Updates the ball's location and velocity.
     *
     * @param timeSlice     double-precision value of the elapsed time, in seconds
     */
    public void move(double timeSlice) {
        centerLocation[0] += currentVelocity[0] * timeSlice;
        centerLocation[1] += currentVelocity[1] * timeSlice;
        updateSpeedsForOneTick(timeSlice);
    }

    public boolean getIsInBounds() {
        return isInBounds;
    }

    /**
     * Our venerable "toString()" representation.
     *
     * @return  String-y version of what this Ball is
     */
    @Override public String toString() {
        DecimalFormat dfp = new DecimalFormat("#0.00");
        DecimalFormat dfv = new DecimalFormat("#0.0000");
        String output = "location <" + dfp.format(centerLocation[X_INDEX]) + ", "
                + dfp.format(centerLocation[Y_INDEX]) + ">";

        // Additional tab in case the location string so far is too short.
        if (output.indexOf(">") <= 23) {
            output += "\t";
        }

        if (!isInBounds) {
            output += "\t<out of bounds>";
        } else if (!isStillMoving()) {
            output += "\t<at rest>";
        } else {
            output += "\tvelocity <" + dfv.format(currentVelocity[X_INDEX]) + " X and "
                    + dfv.format(currentVelocity[Y_INDEX]) + " Y> in/sec";
        }

        return output;
    }
}