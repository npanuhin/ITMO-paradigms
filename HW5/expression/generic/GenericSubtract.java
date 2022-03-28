package expression.generic;

public class GenericSubtract<T> extends AbstractGenericBinaryOperator<T> {
    public GenericSubtract(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    public T count(T x, T y, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.subtract(x, y);
    }
}
