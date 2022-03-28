package expression.generic;

import expression.exceptions.DivisionByZeroException;


public class LongEvaluator implements Evaluator<Long> {

    @Override
    public Long add(Long x, Long y) {
        return x + y;
    }

    @Override
    public Long subtract(Long x, Long y) {
        return x - y;
    }

    @Override
    public Long multiply(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long divide(Long x, Long y) throws DivisionByZeroException {
        if (y == 0) {
            throw new DivisionByZeroException(String.format("%d / 0", x, y));
        }
        return x / y;
    }

    @Override
    public Long negate(Long x) {
        return -x;
    }

    @Override
    public Long count(Long x) {
        return (long) Long.bitCount(x);
    }

    @Override
    public Long max(Long x, Long y) {
        return x >= y ? x : y;
    }

    @Override
    public Long min(Long x, Long y) {
        return x <= y ? x : y;
    }

    @Override
    public Long parse(String value) {
        return Long.parseLong(value);
    }

    @Override
    public Long getValue(int value) {
        return (long) value;
    }
}
