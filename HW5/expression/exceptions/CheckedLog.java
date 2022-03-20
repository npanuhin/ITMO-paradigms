package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedLog extends AbstractCheckedBinaryOperator {
    public CheckedLog(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 3;
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
        return "//";
    }

    @Override
    protected int count(final int a, final int b) {
        if (a <= 0) {
            throw new UnsupportedOperationException("Log(a, b) is not defined when a <= 0");
        }

        if (b <= 0 || b == 1) {
            throw new UnsupportedOperationException("Log(a, b) is not defined when b <= 0 or b = 1");
        }

        int res = 0, x = a;
        while (x >= b) {
            x /= b;
            res++;
        }
        return res;
    }

    @Override
    protected BigDecimal count(BigDecimal a, BigDecimal b) {
        throw new UnsupportedOperationException("Log is not defined for BigDecimal");
    }
}
