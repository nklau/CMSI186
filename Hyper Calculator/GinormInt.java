import java.util.ArrayList;
import java.util.Collections;

/**
 * Filename: GinormInt.java
 * Description:
 * Author:
 * Date:
 */
public class GinormInt implements Comparable<GinormInt> {
    // TODO: define your static and instance variables here
    public static final GinormInt ONE = new GinormInt("1");
    public static final GinormInt ZERO = new GinormInt("0");
    public static final GinormInt TEN = new GinormInt("10");
    private boolean negative = false;
    private ArrayList<Byte> digits;

    /**
     * Constructor takes in a string, saves it, checks for a sign character,
     * checks to see if it's all valid digits, and reverses it for later use.
     *
     * @param  value  String value to make into a GinormInt
     */
    public GinormInt(String value) {
        String str = value;
        if (value.contains("-")) {
            negative = true;
            str = value.substring(1);
        }
        String[] strDigits = str.split("(?!^)");
        digits = new ArrayList<Byte>(strDigits.length);
        for (int i = 0; i < strDigits.length; i++) {
            if (!Character.isDigit(strDigits[i].charAt(0))) {
                throw new IllegalArgumentException("Sorry, all characters must be decimal digit or sign characters.");
            }
            digits.add(0, Byte.parseByte(strDigits[i]));
        }
    }

    /**
     * Method to add a GinormInt value passed in as an argument to this GinormInt.
     *
     * @param  otherInt other GinormInt to add to this GinormInt
     * @return GinormInt that's the sum of this GinormInt and the one passed in
     */
    public GinormInt plus(GinormInt otherInt) {
        int compare = compareTo(otherInt);
        int toCarry = 0;
        int digitLength = 0;
        byte added;
        boolean carry = false;
        String endSumString = "";
        ArrayList<Byte> otherDigits = otherInt.getDigits();
        if ((negative && otherInt.getNegative())
                || (!negative && !otherInt.getNegative())) {
            if (otherDigits.size() > digits.size()) {
                digitLength = otherDigits.size();
                for (int i = digits.size(); i < digitLength; i++) {
                    digits.add((byte)0);
                }
            } else {
                digitLength = digits.size();
                for (int i = otherDigits.size(); i < digitLength; i++) {
                    otherDigits.add((byte)0);
                }
            }
            for (int i = 0; i < digitLength; i++) {
                added = 0;
                if (carry) {
                    added += toCarry;
                    carry = false;
                    toCarry = 0;
                }
                added += digits.get(i) + otherDigits.get(i);
                while (added >= 10) {
                    carry = true;
                    toCarry++;
                    added -= 10;
                }
                endSumString += String.valueOf(added);
            }
            if (carry) {
                endSumString += String.valueOf(toCarry);
            }
            GinormInt endSum = new GinormInt(reverseString(endSumString));
            if (negative) {
                endSum.setNegative(true);
            }
            return endSum;
        } else {
            return minus(otherInt);
        }
    }

    /**
     * Method to subtract a GinormInt passed in as an argument to this GinormInt.
     *
     * @param  otherInt other GinormInt to subtract from this GinormInt
     * @return GinormInt that's the difference of this GinormInt and the one passed in
     */
    public GinormInt minus(GinormInt otherInt) {
        int compare = compareTo(otherInt);
        int digitLength = 0;
        boolean setNegative = false;
        int subtracted;
        String differenceString = "";
        ArrayList<Byte> otherDigits = otherInt.getDigits();
        if (!negative && otherInt.getNegative()
                || negative && !otherInt.getNegative()) {
            if (negative) {
                setNegative = true;
            }
            otherInt.setNegative(false);
            negative = false;
            GinormInt temp = plus(otherInt);
            if (setNegative) {
                temp.setNegative(true);
            }
            return temp;
        }
        if (otherDigits.size() > digits.size()) {
            digitLength = otherDigits.size();
            for (int i = digits.size(); i < digitLength; i++) {
                digits.add((byte)0);
            }
        } else {
            digitLength = digits.size();
            for (int i = otherDigits.size(); i < digitLength; i++) {
                otherDigits.add((byte)0);
            }
        }
        if (compare > 0) {
            for (int i = 0; i < digitLength; i++) {
                if (digits.get(i) < otherDigits.get(i)) {
                    digits.set(i + 1, (byte)(digits.get(i + 1) - 1));
                    digits.set(i, (byte)(digits.get(i) + 10));
                }
                subtracted = digits.get(i) - otherDigits.get(i);
                differenceString += String.valueOf(subtracted);
            }
            if (negative) {
                setNegative = true;
            }
        } else if (compare < 0) {
            for (int i = 0; i < digitLength; i++) {
                if (otherDigits.get(i) < digits.get(i)) {
                    otherDigits.set(i + 1, (byte)(otherDigits.get(i + 1) + 1));
                    otherDigits.set(i, (byte)(otherDigits.get(i) + 10));
                }
                subtracted = otherDigits.get(i) - digits.get(i);
                differenceString += String.valueOf(subtracted);
            }
            if (!negative) {
                setNegative = true;
            }
        } else {
            return ZERO;
        }
        GinormInt endDifference = new GinormInt(reverseString(differenceString));
        if (setNegative) {
            endDifference.setNegative(true);
        }
        return endDifference;
    }

    /**
     * Method to multiply a GinormInt passed in as an argument to this GinormInt.
     *
     * @param  otherInt other GinormInt to multiply by this GinormInt
     * @return GinormInt that's the product of this GinormInt and the one passed in
     */
    public GinormInt times(GinormInt otherInt) {
        boolean setNegative = false;
        boolean carry = false;
        int toCarry = 0;
        int multiplied = 0;
        String tempProductStr = "";
        ArrayList<Byte> otherDigits = otherInt.getDigits();
        ArrayList<Byte> longer;
        ArrayList<Byte> shorter;
        GinormInt product = new GinormInt("0");
        if (!negative && otherInt.getNegative()
                || negative && !otherInt.getNegative()) {
            setNegative = true;
        }
        if (digits.size() > otherDigits.size()) {
            longer = new ArrayList(digits);
            shorter = new ArrayList(otherDigits);
        } else {
            longer = new ArrayList(otherDigits);
            shorter = new ArrayList(digits);
        }
        for (int i = 0; i < shorter.size(); i++) {
            tempProductStr = "";
            for (int j = 0; j < longer.size(); j++) {
                multiplied = longer.get(j) * shorter.get(i);
                if (carry) {
                    multiplied += toCarry;
                    toCarry = 0;
                    carry = false;
                }
                while (multiplied >= 10 && j != longer.size() - 1) {
                    multiplied -= 10;
                    carry = true;
                    toCarry++;
                }
                tempProductStr = String.valueOf(multiplied) + tempProductStr;
            }
            for (int k = 0; k < i; k++) {
                tempProductStr += "0";
            }
            if (carry) {
                tempProductStr = String.valueOf(toCarry) + tempProductStr;
            }
            product = product.plus(new GinormInt(tempProductStr));
        }
        if (setNegative) {
            product.setNegative(true);
        }
        return product;
    }

    /**
     * Method to divide a GinormInt passed in as an argument from this GinormInt.
     *
     * @param  otherInt other GinormInt to divide into this GinormInt
     * @return GinormInt that's the (truncated integer) ratio of this GinormInt and the one passed in
     */
    public GinormInt div(GinormInt otherInt) {
        // (Optional) your code here
        //see powerpoint on Brightspace
        throw new UnsupportedOperationException("Sorry, that operation is not yet implemented.");
    }

    /**
     * Method to find the remainder after dividing by a GinormInt passed in.
     *
     * @param  otherInt other GinormInt to divide into this GinormInt to compute the remainder
     * @return GinormInt that's the remainder after dividing the two BigInts
     */
    public GinormInt mod(GinormInt otherInt) {
        // (Optional) your code here
        throw new UnsupportedOperationException("Sorry, that operation is not yet implemented.");
    }

    /**
     * Method to compare this GinormInt to another GinormInt passed in.
     *
     * @param  otherInt other GinormInt to compare to
     * @return 1 if this GinormInt is larger, 0 if equal, -1 if it's smaller
     */
    @Override
    public int compareTo(GinormInt otherInt) {
        if (negative && !otherInt.getNegative() || !negative && otherInt.getNegative()) {
            if (negative) {
                return -1;
            }
            return 1;
        }
        ArrayList<Byte> otherDigits = otherInt.getDigits();
        if (digits.size() < otherDigits.size()) {
            if (negative) {
                return 1;
            }
            return -1;
        }
        if (digits.size() > otherDigits.size()) {
            if (negative) {
                return -1;
            }
            return 1;
        }
        for (int i = digits.size() - 1; i >= 0; i--) {
            if (digits.get(i) > otherDigits.get(i)) {
                return 1;
            } else if (digits.get(i) < otherDigits.get(i)) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Method to check if this GinormInt equals another GinormInt passed in.
     *
     * @param  otherInt other GinormInt to compare to
     * @return true if they're equal, false otherwise
     */
    public boolean equals(GinormInt otherInt) {
        if (compareTo(otherInt) == 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if GinormInt is negative, false otherwise.
     */
    public boolean getNegative() {
        return negative;
    }

    /**
     * Sets negative to true or false.
     */
    public void setNegative(boolean newNegative) {
        negative = newNegative;
    }

    /**
     * Returns the GinormInt's digits.
     */
    public ArrayList<Byte> getDigits() {
        ArrayList<Byte> toReturn = new ArrayList<>();
        for (byte num : digits) {
            toReturn.add(num);
        }
        return toReturn;
    }

    /**
     * Takes a String and returns a reversed String.
     */
    public String reverseString(String str) {
        String reversed = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed += str.charAt(i);
        }
        return reversed;
    }

    /**
     * Method to return the string representation of this GinormInt.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        String str = "";
        for (byte i : digits) {
            str += String.valueOf(i);
        }
        if (negative) {
            str += "-";
        }
        return reverseString(str);
    }
}