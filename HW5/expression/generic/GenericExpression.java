package expression.generic;

public interface GenericExpression<T> {
    T evaluate(T x, T y, T z, Evaluator<T> evaluator) throws ArithmeticException;
}
