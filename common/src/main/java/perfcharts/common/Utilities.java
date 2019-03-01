package perfcharts.common;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * A class that contains many help functions.
 *
 * @author Rayson Zhu
 */
public class Utilities {
    private static final Pattern longPattern = Pattern.compile("[+-]?\\d+");
    private static final Pattern doublePattern = Pattern
            .compile("[+-]?\\d*\\.\\d+([Ee][+-]?\\d)?|NaN");

    /**
     * determine if the string s can be converted to a {@link Double}
     *
     * @param s a string
     * @return true if can, otherwise false
     */
    public static boolean isDouble(String s) {
        return doublePattern.matcher(s).matches();
    }

    /**
     * determine if the string s can be converted to a {@link Long}
     *
     * @param s a string
     * @return true if can, otherwise false
     */
    public static boolean isLong(String s) {
        return longPattern.matcher(s).matches();
    }

    /**
     * parse a string to an Java object
     *
     * @param s a string
     * @return the parsed object
     */
    public static Object parseString(String s) {
        if (Utilities.isDouble(s))
            return parseDouble(s);
        if (Utilities.isLong(s)) {
            // if (s.length() > 9)
            return Long.parseLong(s);
            // return Integer.parseInt(s);
        }
        return s;
    }

    /**
     * parse a string to an Java object
     *
     * @param s     a string
     * @param clazz the destination type
     * @return the parsed object
     */
    public static Object parseString(String s, Class<?> clazz) {
        Object value;
        if (clazz == Integer.class || clazz == int.class)
            value = Integer.parseInt(s);
        else if (clazz == Double.class || clazz == double.class)
            value = parseDouble(s);
        else if (clazz == Long.class || clazz == long.class)
            value = Long.parseLong(s);
        else if (clazz == Float.class || clazz == float.class)
            value = Float.parseFloat(s);
        else
            value = s;
        return value;
    }

    public static double parseDouble(String s) {
        switch (s) {
            case "+∞":
                return Double.POSITIVE_INFINITY;
            case "-∞":
                return Double.NEGATIVE_INFINITY;
            case "NaN":
            case "":
                return Double.NaN;
            default:
                return Double.parseDouble(s);
        }

    }

    public static String doubleToString(double d) {
        if (Double.POSITIVE_INFINITY == d)
            return "+∞";
        else if (Double.NEGATIVE_INFINITY == d)
            return "-∞";
        else if (Double.isNaN(d))
            return "NaN";
        return Double.toString(d);
    }

    /**
     * parse a string to an Java object
     *
     * @param s    a string
     * @param type the destination type
     * @return the parsed object
     */
    public static Object parseString(String s, String type) {
        Object value;
        if (type.equals("int"))
            value = Integer.parseInt(s);
        else if (type.equals("double"))
            value = parseDouble(s);
        else if (type.equals("long"))
            value = Long.parseLong(s);
        else if (type.equals("float"))
            value = Float.parseFloat(s);
        else
            value = s;
        return value;
    }

    /**
     * convert a Java object to JSON value
     *
     * @param o an object
     * @return a JSON value
     */
    public static String commonConvertToJsonValue(Object o) {
        if (o == null)
            return "null";
        if (o instanceof Integer || o instanceof Long)
            return o.toString();
        if (o instanceof Double || o instanceof Float)
            return String.format("%.3f", o);
        if (o instanceof Boolean)
            return ((Boolean) o).booleanValue() ? "true" : "false";
        return "\"" + o.toString().replace("\"", "\\\"") + "\"";
    }

    /**
     * An implementation of randomized partition based selection algorithm
     * See https://en.wikipedia.org/wiki/Selection_algorithm#Partition-based_selection
     *
     * @param collection a collection
     * @param targetRank a index
     * @return the selected element
     */
    public static Number fastSelect(Collection<Long> collection, int targetRank) {
        if (collection.isEmpty())
            return null;
        ArrayList<Long> copy = new ArrayList<Long>(collection);
        int start = 0, end = copy.size();
        // do partitioning and check the position of returned pivot, until
        // getting the one we want.
        while (start < end) {
            int pivot = partition(copy, start, end);
            if (pivot < targetRank) {
                start = pivot + 1;
            } else if (pivot > targetRank) {
                end = pivot;
            } else {
                return copy.get(pivot);
            }
        }
        return null;
    }

    private static Random random = new Random();

    /**
     * the classical partition function (used by #fastSelect(Collection, int))
     *
     * @param arr   an array
     * @param start that start index of arr (inclusive)
     * @param end   that end index of arr (exclusive)
     * @return the index of pivot
     */
    private static <T> int partition(ArrayList<Long> arr, int start, int end) {
        if (end <= start || start < 0 || end > arr.size() || arr.isEmpty())
            throw new InvalidParameterException(
                    "The precondition does not meet: 0<=start<end<=arr.size()");
        // the left and right boundaries default to start and end - 1
        int left = start, right = end - 1;
        // choose a pivot in [start, end)
        int pivot = start + random.nextInt(end - start);
        // swap arr[left] with arr[pivot]
        Long pivotValue = arr.get(pivot);
        arr.set(pivot, arr.get(left));
        // do the partitioning
        while (left < right) {
            while (left < right && arr.get(right) >= pivotValue)
                --right;
            arr.set(left, arr.get(right));
            while (left < right && arr.get(left) <= pivotValue)
                ++left;
            arr.set(right, arr.get(left));
        }
        arr.set(left, pivotValue);
        return left;
    }
}
