package expression;

import java.math.BigDecimal;


public class Multiply extends AbstractBinaryOperator {
    public Multiply(AbstractExpression left, AbstractExpression right) {
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

    @Override
    protected int count(final int a, final int b) {
        return a * b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.multiply(b);
    }
}
