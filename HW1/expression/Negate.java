package expression;

import java.math.BigDecimal;


public class Negate extends AbstractUnaryOperator {
    public Negate(AbstractExpression content) {
        super(content);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    protected int count(final int a) {
        return -a;
    }

    @Override
    protected BigDecimal count(final BigDecimal a) {
        return a.negate();
    }
}
