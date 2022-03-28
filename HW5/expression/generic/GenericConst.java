package expression.generic;

public class GenericConst<T> implements GenericExpression<T> {
    private final String value;

    public GenericConst(String content) {
        value = content;
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        return evaluator.parse(value);
    }
}
