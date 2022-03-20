package expression.exceptions;

import java.math.BigDecimal;

import expression.AbstractExpression;


public class CheckedNegate extends AbstractCheckedUnaryOperator {
    public CheckedNegate(AbstractExpression content) {
        super(content);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    protected int count(final int a) throws OverflowException {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException(String.format("-%d", a));
        }
        return -a;
    }

    @Override
    protected BigDecimal count(BigDecimal a) {
        return a.negate();
    }
}
