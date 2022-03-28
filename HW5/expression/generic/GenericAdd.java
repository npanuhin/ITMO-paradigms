package expression.generic;

public class GenericAdd<T> extends AbstractGenericBinaryOperator<T> {
    public GenericAdd(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    public T count(T x, T y, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.add(x, y);
    }
}
