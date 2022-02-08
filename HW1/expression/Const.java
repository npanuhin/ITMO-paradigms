package expression;

import java.math.BigDecimal;


public class Const implements AbstractExpression {
    private final int value;
    private final BigDecimal decimalValue;

    public Const(int value) {
        this.value = value;
        this.decimalValue = null;
    }

    public Const(BigDecimal value) {
        this.value = 0;
        this.decimalValue = value;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public boolean isAssociative() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Associativity is not defined for Const");
    }

    @Override
    public boolean alwaysNeedsWrap() {
        return false;
    }

    @Override
    public String toString() {
        return (decimalValue == null ? Integer.toString(value) : decimalValue.toString());
    }

    @Override
    public int evaluate(final int x) {
        return value;
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return value;
    }

    // @Override
    // public BigDecimal evaluate(final BigDecimal x) {
    //     return decimalValue == null ? new BigDecimal(value) : decimalValue;
    // }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            if (this.decimalValue == null) {
                return value == ((Const) obj).value;
            } else {
                return decimalValue.equals(((Const) obj).decimalValue);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return decimalValue == null ? value : decimalValue.hashCode();
    }
}
