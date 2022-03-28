package expression.generic;

public class GenericMultiply<T> extends AbstractGenericBinaryOperator<T> {
    public GenericMultiply(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    public T count(T x, T y, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.multiply(x, y);
    }
}
