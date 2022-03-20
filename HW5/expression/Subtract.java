package expression;

import java.math.BigDecimal;


public class Subtract extends AbstractBinaryOperator {
    public Subtract(AbstractExpression left, AbstractExpression right) {
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
    protected int count(final int a, final int b) {
        return a - b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.subtract(b);
    }
}
