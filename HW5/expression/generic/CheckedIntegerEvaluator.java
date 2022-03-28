package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;


public class CheckedIntegerEvaluator extends IntegerEvaluator {

    @Override
    public Integer add(Integer x, Integer y) throws OverflowException {
        if (
            (y > 0 && Integer.MAX_VALUE - y < x) ||
            (y < 0 && Integer.MIN_VALUE - y > x)
        ) {
            throw new OverflowException(String.format("%d + %d", x, y));
        }
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) throws OverflowException {
        if (
            (y > 0 && Integer.MIN_VALUE + y > x) ||
            (y < 0 && Integer.MAX_VALUE + y < x)
        ) {
            throw new OverflowException(String.format("%d - %d", x, y));
        }
        return x - y;
    }

    protected static void checkMultiplication(final int x, final int y) throws OverflowException {
        final int maximum = Integer.signum(x) == Integer.signum(y) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        if (x == -1 && y == Integer.MIN_VALUE) {
            throw new OverflowException(String.format("%d * %d", x, y));

        } else if (x != -1 && x != 0 && (
            (y > 0 && y > maximum / x) ||
            (y < 0 && y < maximum / x)
        )) {
            throw new OverflowException(String.format("%d * %d", x, y));
        }
    }

    @Override
    public Integer multiply(Integer x, Integer y) throws OverflowException {
        checkMultiplication(x, y);
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) throws OverflowException, DivisionByZeroException {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException(String.format("%d / %d", x, y));
        }
        if (y == 0) {
            throw new DivisionByZeroException(String.format("%d / 0", x));
        }
        return x / y;
    }

    @Override
    public Integer negate(Integer x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException(String.format("-%d", x));
        }
        return -x;
    }
}
