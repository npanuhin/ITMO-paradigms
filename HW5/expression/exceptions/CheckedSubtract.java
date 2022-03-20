package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedSubtract extends AbstractCheckedBinaryOperator {
    public CheckedSubtract(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean alwaysNeedsWrap() {
        return false;
    }

    @Override
    protected String getOperator() {
        return "-";
    }

    @Override
    protected int count(final int a, final int b) throws OverflowException {
        if (
            (b > 0 && Integer.MIN_VALUE + b > a) ||
            (b < 0 && Integer.MAX_VALUE + b < a)
        ) {
            throw new OverflowException(String.format("%d - %d", a, b));
        }
        return a - b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.subtract(b);
    }
}
