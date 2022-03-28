package expression.generic;

import java.math.BigInteger;

import expression.exceptions.DivisionByZeroException;


public class BigIntegerEvaluator implements Evaluator<BigInteger> {

    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) throws DivisionByZeroException {
        if (y == BigInteger.ZERO) {
            throw new DivisionByZeroException(String.format("%d / 0", x, y));
        }
        return x.divide(y);
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger count(BigInteger x) {
        return BigInteger.valueOf(x.bitCount());
    }

    @Override
    public BigInteger max(BigInteger x, BigInteger y) {
        return x.max(y);
    }

    @Override
    public BigInteger min(BigInteger x, BigInteger y) {
        return x.min(y);
    }

    @Override
    public BigInteger parse(String value) {
        return new BigInteger(value);
    }

    @Override
    public BigInteger getValue(int value) {
        return BigInteger.valueOf(value);
    }
}
