package expression.exceptions;

import expression.AbstractUnaryOperator;
import expression.AbstractExpression;


public abstract class AbstractCheckedUnaryOperator extends AbstractUnaryOperator {

    public AbstractCheckedUnaryOperator(AbstractExpression content) {
        super(content);
    }

    protected abstract int count(int content) throws OverflowException;

    @Override
    public int evaluate(final int x) throws OverflowException {
        return count(content.evaluate(x));
    }

    protected void throwOverflowException() {
        throw new OverflowException(String.format(getOperator() + "(%d)", content));
    }
}
