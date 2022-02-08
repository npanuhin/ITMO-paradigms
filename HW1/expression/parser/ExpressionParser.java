package expression.parser;

import java.util.Arrays;
import java.util.Set;

import expression.*;


public class ExpressionParser implements TripleParser {
    private static final Set<Character> VARIABLES = Set.of('x', 'y', 'z');
    private AbstractStreamer expr;

    private boolean isDigit(final char c) {
        // return Character.isDigit(c);
        return '0' <= c && c <= '9';
    }

    private void skipWhitespaces() {
        while (expr.hasNextChar() && Character.isWhitespace(expr.curChar())) {
            expr.getChar();
        }
    }

    private int parseInt(final boolean isNegative) {
        StringBuilder number = new StringBuilder();
        if (isNegative) {
            number.append('-');
        }
        while (expr.hasNextChar() && isDigit(expr.curChar())) {
            number.append(expr.getChar());
        }
        return Integer.parseInt(number.toString());
    }

    private AbstractExpression parsePriority3() {
        skipWhitespaces();

        if (expr.skipIfMatch('(')) {
            final AbstractExpression result = parsePriority0();
            expr.expect(')');
            return result;
        }

        if (expr.skipIfMatch('l')) {
            expr.expect('0');
            return new LeadingZeros(parsePriority3());

        } else if (expr.skipIfMatch('t')) {
            expr.expect('0');
            return new TrailingZeros(parsePriority3());

        } else if (expr.skipIfMatch('-')) {
            if (isDigit(expr.curChar())) {
                return new Const(parseInt(true));
            } else {
                return new Negate(parsePriority3());
            }

        } else if (isDigit(expr.curChar())) {
            return new Const(parseInt(false));

        } else if (VARIABLES.contains(expr.curChar())) {
            return new Variable(expr.getChar());

        } else {
            throw new IllegalArgumentException(
                "Can not identify expression starting with char: \"" + expr.curChar() + "\"..."
            );
        }
    }

    private AbstractExpression parsePriority2() {
        AbstractExpression result = parsePriority3();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('*')) {
                result = new Multiply(result, parsePriority3());
            } else if (expr.skipIfMatch('/')) {
                result = new Divide(result, parsePriority3());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private AbstractExpression parsePriority1() {
        AbstractExpression result = parsePriority2();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('+')) {
                result = new Add(result, parsePriority2());
            } else if (expr.skipIfMatch('-')) {
                result = new Subtract(result, parsePriority2());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private AbstractExpression parsePriority0() {
        AbstractExpression result = parsePriority1();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('>')) {
                expr.expect('>');
                if (expr.skipIfMatch('>')) {
                    result = new RightArithmeticBitshift(result, parsePriority1());
                } else {
                    result = new RightBitshift(result, parsePriority1());
                }
            } else if (expr.skipIfMatch('<')) {
                expr.expect('<');
                result = new LeftBitshift(result, parsePriority1());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    @Override
    public AbstractExpression parse(String expression) {
        this.expr = new StringStreamer(expression);
        return parsePriority0();
    }
}
