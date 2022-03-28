package expression.generic;

public class GenericMax<T> extends AbstractGenericBinaryOperator<T> {
    public GenericMax(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    public T count(T x, T y, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.max(x, y);
    }
}
