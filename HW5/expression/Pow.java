package expression;

import java.math.BigDecimal;


public class Pow extends AbstractBinaryOperator {
    public Pow(AbstractExpression left, AbstractExpression right) {
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
        return "**";
    }

    @Override
    protected int count(final int a, final int b) {
        return (int) Math.pow(a, b);
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        return a.pow(b.intValue());
    }
}
