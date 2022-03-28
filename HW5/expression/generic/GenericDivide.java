package expression.generic;

public class GenericDivide<T> extends AbstractGenericBinaryOperator<T> {
    public GenericDivide(GenericExpression<T> left, GenericExpression<T> right) {
        super(left, right);
    }

    @Override
    public T count(T x, T y, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.divide(x, y);
    }
}
