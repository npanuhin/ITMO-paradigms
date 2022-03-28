package expression.generic;

public abstract class AbstractGenericUnaryOperator<T> implements GenericExpression<T> {
    protected final GenericExpression<T> content;

    public AbstractGenericUnaryOperator(GenericExpression<T> content) {
        this.content = content;
    }

    protected abstract T count(T content, Evaluator<T> evaluator) throws ArithmeticException;

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) throws ArithmeticException {
        return count(content.evaluate(x, y, z, evaluator), evaluator);
    }
}
