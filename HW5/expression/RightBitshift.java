package expression;

import java.math.BigDecimal;


public class RightBitshift extends AbstractBinaryOperator {
    public RightBitshift(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    @Override
    public int getPriority() {
        return 0;
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
        return ">>";
    }

    @Override
    protected int count(final int a, final int b) {
        return a >> b;
    }

    @Override
    protected BigDecimal count(final BigDecimal a, final BigDecimal b) {
        throw new UnsupportedOperationException("Bitshift is not defined for BigDecimal");
    }
}
