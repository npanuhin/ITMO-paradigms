package expression.generic;

import expression.exceptions.OverflowException;


public class GenericNegate<T> extends AbstractGenericUnaryOperator<T> {
    public GenericNegate(GenericExpression<T> content) {
        super(content);
    }

    @Override
    public T count(T x, Evaluator<T> evaluator) throws ArithmeticException {
        return evaluator.negate(x);
    }
}
