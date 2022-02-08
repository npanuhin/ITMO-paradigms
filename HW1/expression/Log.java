package expression;

import java.math.BigDecimal;


public class Log extends AbstractBinaryOperator {
    public Log(AbstractExpression left, AbstractExpression right) {
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
        return (int) (Math.log(a) / Math.log(b));
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        throw new UnsupportedOperationException("Log is not defined for BigDecimal");
    }
}
