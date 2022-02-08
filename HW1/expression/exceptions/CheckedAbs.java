package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedAbs extends AbstractCheckedUnaryOperator {
    public CheckedAbs(AbstractExpression content) {
        super(content);
    }

    @Override
    public String getOperator() {
        return "abs";
    }

    @Override
    protected int count(final int a) throws OverflowException {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException(String.format("abs(%d)", a));
        }
        return a < 0 ? -a : a;
    }

    @Override
    protected BigDecimal count(final BigDecimal a) {
        return a.abs();
    }
}
