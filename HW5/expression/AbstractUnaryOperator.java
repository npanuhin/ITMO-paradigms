package expression;

import java.math.BigDecimal;
import java.util.Objects;


public abstract class AbstractUnaryOperator implements AbstractExpression {
    protected final AbstractExpression content;
    private String cachedToString = null, cachedToMiniString = null;

    public AbstractUnaryOperator(AbstractExpression content) {
        this.content = content;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public boolean isAssociative() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Associativity is not defined for unary operators");
    }

    @Override
    public boolean alwaysNeedsWrap() {
        return false;
    }

    protected abstract String getOperator();
    protected abstract int count(int content);
    protected abstract BigDecimal count(BigDecimal content);

    @Override
    public int evaluate(final int x) {
        return count(content.evaluate(x));
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return count(content.evaluate(x, y, z));
    }

    // @Override
    // public BigDecimal evaluate(final BigDecimal x) {
    //     return count(content.evaluate(x));
    // }

    @Override
    public String toString() {
        if (cachedToString == null) {
            cachedToString = getOperator() + '(' + content.toString() + ')';
        }
        return cachedToString;
    }

    @Override
    public String toMiniString() {
        if (cachedToMiniString == null) {
            if (this.getPriority() > content.getPriority()) {
                cachedToMiniString = getOperator() + '(' + content.toMiniString() + ')';
            } else {
                cachedToMiniString = getOperator() + ' ' + content.toMiniString();
            }
        }
        return cachedToMiniString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            return content.equals(((AbstractUnaryOperator) obj).content);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getClass()) + 17 * content.hashCode();
    }
}
