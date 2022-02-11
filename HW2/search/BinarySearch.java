package search;


public class BinarySearch {
    public static int binsearch_iterative(final int[] array, final int x) {
        if (array.length == 0 || array[array.length - 1] > x) {
            return array.length;
        }

        int left = -1, right = array.length - 1, middle;
        while (right - left > 1) {
            middle = (left + right) / 2;
            if (array[middle] > x) {
                left = middle;
            } else {
                right = middle;
            }
        }

        return right;
    }

    private static int binsearch_recursive_inner(final int[] array, final int x, final int left, final int right) {
        if (right - left <= 1) {
            return right;
        }

        final int middle = (left + right) / 2;

        if (array[middle] > x) {
            return binsearch_recursive_inner(array, x, middle, right);
        } else {
            return binsearch_recursive_inner(array, x, left, middle);
        }
    }

    public static int binsearch_recursive(final int[] array, final int x) {
        if (array.length == 0 || array[array.length - 1] > x) {
            return array.length;
        }
        return binsearch_recursive_inner(array, x, -1, array.length - 1);
    }

    public static void main(String[] args) {

        final int x = Integer.parseInt(args[0]);

        final int[] array = new int[args.length - 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(args[i + 1]);
        }

        System.out.println(binsearch_iterative(array, x));
        // System.out.println(binsearch_recursive(array, x));
    }
}
