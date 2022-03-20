package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedAdd extends AbstractCheckedBinaryOperator {
    public CheckedAdd(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 1;
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
        return "+";
    }

    @Override
    protected int count(final int a, final int b) {
        if (
            (b > 0 && Integer.MAX_VALUE - b < a) ||
            (b < 0 && Integer.MIN_VALUE - b > a)
        ) {
            throwOverflowException();
        }
        return a + b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.add(b);
    }
}
