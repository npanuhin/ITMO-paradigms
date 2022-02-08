package expression;

import java.math.BigDecimal;


public class TrailingZeros extends AbstractUnaryOperator {
    public TrailingZeros(AbstractExpression content) {
        super(content);
    }

    @Override
    public String getOperator() {
        return "t0";
    }

    @Override
    protected int count(final int a) {
        return Integer.numberOfTrailingZeros(a);
    }

    @Override
    protected BigDecimal count(final BigDecimal a) {
        throw new UnsupportedOperationException("Number of trailing zeros is not defined for BigDecimal");
    }
}
