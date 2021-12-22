/**
 * Filename: PiEstimator.java
 * Description: Estimate pi based on randomly thrown darts.
 * Author: Natalie Lau
 * Date: 01/22/21
 */
public class PiEstimator {
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Exactly one argument required");
            } else if (Integer.parseInt(args[0]) < 1) {
                throw new IllegalArgumentException("At least one dart required");
            }

            int totalDarts = Integer.parseInt(args[0]);
            String piString = Double.toString(estimate(totalDarts));
            System.out.println(piString);

        } catch (NumberFormatException e) {
            System.err.println("Argument must be an integer");

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static double estimate(int darts) {
        if (darts <= 1) {
            throw new IllegalArgumentException("At least one dart required");
        }

        int totalHits = 0;

        for (int i = 0; i < darts; i++) {
            boolean inCircle = throwDart();
            if (inCircle) {
                totalHits++;
            }
        }
        double pi = (double)totalHits / (double)darts * 4;
        return pi;
    }

    public static boolean throwDart() {
        double x = Math.random() - 1;
        double y = Math.random() - 1;
        if (Math.pow(x, 2) + Math.pow(y, 2) <= 1) {
            return true;
        }
        return false;
    }
}