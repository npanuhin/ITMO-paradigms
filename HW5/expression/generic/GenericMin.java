package expression.generic;

public class GenericMin<T> extends AbstractGenericBinaryOperator<T> {
    public GenericMin(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    public T count(T x, T y, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.min(x, y);
    }
}
