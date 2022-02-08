package expression;

import java.math.BigDecimal;
import java.util.Objects;


public abstract class AbstractBinaryOperator implements AbstractExpression {
    protected final AbstractExpression left, right;
    private String cachedToString = null, cachedToMiniString = null;

    public AbstractBinaryOperator(AbstractExpression left, AbstractExpression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getOperator();
    protected abstract int count(int left, int right);
    protected abstract BigDecimal count(BigDecimal left, BigDecimal right);

    @Override
    public int evaluate(final int x) {
        return count(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return count(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    // @Override
    // public BigDecimal evaluate(final BigDecimal x) {
    //     return count(left.evaluate(x), right.evaluate(x));
    // }

    @Override
    public String toString() {
        if (cachedToString == null) {
            final StringBuilder result = new StringBuilder();

            result.append('(').append(left).append(' ').append(getOperator()).append(' ').append(right).append(')');

            cachedToString = result.toString();
        }
        return cachedToString;
    }

    @Override
    public String toMiniString() {
        if (cachedToMiniString == null) {
            final StringBuilder result = new StringBuilder();

            // Adding left operand
            addMiniExprToBuilder(result, left, (this.getPriority() > left.getPriority()));

            // Adding operator
            result.append(' ').append(getOperator()).append(' ');

            // Adding right operand
            if (this.getPriority() < right.getPriority()) {
                addMiniExprToBuilder(result, right, false);

            } else if (this.alwaysNeedsWrap() || right.alwaysNeedsWrap()) {
                addMiniExprToBuilder(result, right, true);

            } else if (this.getPriority() == right.getPriority()) {
                addMiniExprToBuilder(result, right, !this.isAssociative());

            } else {
                addMiniExprToBuilder(result, right, true);
            }

            cachedToMiniString = result.toString();
        }
        return cachedToMiniString;
    }

    private void addMiniExprToBuilder(final StringBuilder builder, final AbstractExpression expr,  final boolean isWrapped) {
        if (isWrapped) {
            builder.append('(');
        }
        builder.append(expr.toMiniString());
        if (isWrapped) {
            builder.append(')');
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return left.equals(((AbstractBinaryOperator) obj).left)
                && right.equals(((AbstractBinaryOperator) obj).right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getClass()) + 17 * (left.hashCode() + 17 * right.hashCode());
    }
}
