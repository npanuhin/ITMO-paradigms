package expression;

import java.math.BigDecimal;


public class Abs extends AbstractUnaryOperator {
    public Abs(AbstractExpression content) {
        super(content);
    }

    @Override
    public String getOperator() {
        return "abs";
    }

    @Override
    protected int count(int a) {
        return a < 0 ? -a : a;
    }

    @Override
    protected BigDecimal count(BigDecimal a) {
        return a.abs();
    }
}
