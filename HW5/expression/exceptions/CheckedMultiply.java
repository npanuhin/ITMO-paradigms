package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedMultiply extends AbstractCheckedBinaryOperator {
    public CheckedMultiply(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }

    @Override
    public boolean alwaysNeedsWrap() {
        return false;
    }

    @Override
    protected String getOperator() {
        return "*";
    }

    protected static void checkMultiplication(final int a, final int b) throws OverflowException {
        final int maximum = Integer.signum(a) == Integer.signum(b) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        if (a == -1 && b == Integer.MIN_VALUE) {
            throw new OverflowException(String.format("%d * %d", a, b));

        } else if (a != -1 && a != 0 && (
            (b > 0 && b > maximum / a) ||
            (b < 0 && b < maximum / a)
        )) {
            throw new OverflowException(String.format("%d * %d", a, b));
        }
    }

    @Override
    protected int count(final int a, final int b) throws OverflowException {
        checkMultiplication(a, b);
        return a * b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.multiply(b);
    }
}
