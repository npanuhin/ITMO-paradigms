package expression.exceptions;

import expression.AbstractBinaryOperator;
import expression.AbstractExpression;


public abstract class AbstractCheckedBinaryOperator extends AbstractBinaryOperator {

    public AbstractCheckedBinaryOperator(AbstractExpression left, AbstractExpression right) {
        super(left, right);
    }

    protected abstract int count(int left, int right) throws OverflowException;

    @Override
    public int evaluate(final int x) throws OverflowException {
        return count(left.evaluate(x), right.evaluate(x));
    }

    protected void throwOverflowException() {
        throw new OverflowException(String.format("%d " + getOperator() + " %d", left, right));
    }
}
