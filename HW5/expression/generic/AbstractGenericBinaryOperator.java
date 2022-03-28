package expression.generic;

public abstract class AbstractGenericBinaryOperator<T> implements GenericExpression<T> {
    protected final GenericExpression<T> left, right;

    public AbstractGenericBinaryOperator(GenericExpression<T> left, GenericExpression<T> right) {
        this.left = left;
        this.right = right;
    }

    protected abstract T count(T left, T right, Evaluator<T> evaluator) throws ArithmeticException;

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) throws ArithmeticException {
        return count(
            left.evaluate(x, y, z, evaluator),
            right.evaluate(x, y, z, evaluator),
            evaluator
        );
    }
}
