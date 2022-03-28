package expression.generic;

import expression.exceptions.DivisionByZeroException;


public class IntegerEvaluator implements Evaluator<Integer> {

    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) throws DivisionByZeroException {
        if (y == 0) {
            throw new DivisionByZeroException(String.format("%d / 0", x));
        }
        return x / y;
    }

    @Override
    public Integer negate(Integer x) {
        return -x;
    }

    @Override
    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return x >= y ? x : y;
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return x <= y ? x : y;
    }

    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Integer getValue(int value) {
        return value;
    }
}
