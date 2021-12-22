import java.util.ArrayList;

/**
 * Filename: SkateRamp.java
 * Description: Estimates the area under a curve.
 * Author: Natalie Lau
 * Date: 14 March 2021
 */
public class SkateRamp {
    private int numCoeff;
    private double percentage = 1;
    private double lowerBound;
    private double upperBound;
    private ArrayList<Double> coeffs = new ArrayList<>();
    private String functionType;

    /**
     *  The interface Function will be implemented by Polynomial and Sine.
     *  The concrete classes must implement the getY() method.
     */
    interface Function {
        double getY(double x);
    }

    class Polynomial implements Function {
        /**
         * Calculates y, given x, for a polynomial curve.
         */
        public double getY(double x) {
            double y = 0;
            for (int i = 0; i < numCoeff; i++) {
                y += Math.pow(x, i) * coeffs.get(i);
            }
            return y;
        }
    }

    class Sine implements Function {
        /**
         * Calculates y, given x, for a sine curve.
         */
        public double getY(double x) {
            return Math.sin(x);
        }
    }

    /**
     * SkateRamp constructor: validates and sets up instance variables from args.
     */
    public SkateRamp(String... args) throws NumberFormatException, IllegalArgumentException {
        boolean customPercent = false;
        if (args.length < 3) {
            throw new IllegalArgumentException("Must provide at least 3 args");
        }
        functionType = args[0].toLowerCase();
        if (!functionType.equals("poly") && !functionType.equals("sin")) {
            throw new IllegalArgumentException("Unrecognized function");
        }
        if (args[args.length - 1].contains("%")) {
            String percentString = args[args.length - 1];
            percentage = Double.parseDouble(percentString.substring(0, percentString.length() - 1));
            customPercent = true;
            lowerBound = Double.parseDouble(args[args.length - 3]);
            upperBound = Double.parseDouble(args[args.length - 2]);
            numCoeff = args.length - 4;
            if (percentage <= 0) {
                throw new NumberFormatException("% must be positive");
            }
        } else {
            lowerBound = Double.parseDouble(args[args.length - 2]);
            upperBound = Double.parseDouble(args[args.length - 1]);
            numCoeff = args.length - 3;
        }
        if (lowerBound > upperBound) {
            throw new NumberFormatException("Upper bound must be > lower bound");
        }
        if (functionType.equals("poly")) {
            if (customPercent && args.length < 5 || !customPercent && args.length < 4) {
                throw new IllegalArgumentException("Need at least 1 coeff for poly");
            }
        }
        for (int i = 0; i < numCoeff; i++) {
            coeffs.add(Double.parseDouble(args[i + 1]));
        }
    }

    /**
     * Estimates the area under the curve by calculating the area under an
     * increasing number of rectangles, until 2 areas are within 1% of each other.
     *
     * @return the estimated area
     */
    public double estimateAreaUnderRamp() {
        double estimate = Integer.MIN_VALUE;
        double last = Integer.MAX_VALUE;
        int iteration = 0;
        while (functionType.equals("poly")
                && Math.round(Math.abs(last - estimate) * 1000000d) / 1000000d >= (percentage / 100)
                || !functionType.equals("poly") && Math.abs(last - estimate) >= (percentage / 100)) {
            last = estimate;
            double width;
            double x;
            double height;
            iteration++;
            width = (upperBound - lowerBound) / iteration;
            if (functionType.equals("poly")) {
                estimate = 0;
                x = lowerBound + (width / 2);
                for (int i = 0; i < iteration; i++) {
                    Polynomial poly = new Polynomial();
                    height = poly.getY(x);
                    estimate += width * height;
                    x += width;
                }
            } else {
                estimate = 0;
                x = lowerBound + (width / 2);
                for (int i = 0; i < iteration; i++) {
                    Sine sin = new Sine();
                    height = sin.getY(x);
                    estimate += width * height;
                    x += width;
                }

            }
            System.out.println(last);
        }
        System.out.println(iteration);
        return last;
    }

    /**
     * main() creates a new SkateRamp object and calls estimateAreaUnderRamp().
     */
    public static void main(String[] args) {
        try {
            SkateRamp ramp = new SkateRamp(args);
            double estimate = ramp.estimateAreaUnderRamp();
            System.out.println(estimate);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
