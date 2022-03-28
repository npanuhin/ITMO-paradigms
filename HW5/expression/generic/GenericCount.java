package expression.generic;

public class GenericCount<T> extends AbstractGenericUnaryOperator<T> {
    public GenericCount(GenericExpression<T> content) {
        super(content);
    }

    @Override
    public T count(T x, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.count(x);
    }
}
