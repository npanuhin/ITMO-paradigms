package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedDivide extends AbstractCheckedBinaryOperator {
    public CheckedDivide(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean alwaysNeedsWrap() {
        return true;
    }

    @Override
    protected String getOperator() {
        return "/";
    }

    @Override
    protected int count(final int a, final int b) throws OverflowException {
        if (a == Integer.MIN_VALUE && b == -1) {
            throwOverflowException();
        }
        if (b == 0) {
            throw new DivisionByZeroException(String.format("%d / 0", a));
        }
        return a / b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.divide(b);
    }
}
