package expression.exceptions;

import java.util.Arrays;
import java.util.Set;

import expression.*;
import expression.parser.*;


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

    private int parseInt(final boolean isNegative) throws ParseException {
        StringBuilder number = new StringBuilder();
        if (isNegative) {
            number.append('-');
        }
        while (expr.hasNextChar() && isDigit(expr.curChar())) {
            number.append(expr.getChar());
        }
        try {
            return Integer.parseInt(number.toString());
        } catch (NumberFormatException e) {
            throw new ParseException("Overflow in parseInt(" + number + ")");
        }
    }

    private AbstractExpression parsePriority4() throws ParseException {
        skipWhitespaces();

        if (!expr.hasNextChar()) {
            throw new ParseException(
                "Unexpected end of stream, expected Const, Variable or any other Expression at the end"
            );
        }

        if (expr.skipIfMatch('(')) {
            final AbstractExpression result = parsePriority0();
            expr.expect(')');
            return result;
        }

        if (expr.skipIfMatch("abs")) {
            if (!expr.hasNextChar() || !(Character.isWhitespace(expr.curChar()) || expr.curChar() == '(')) {
                throw new ParseException("Expected whitespace or '(' after Abs");
            }
            return new CheckedAbs(parsePriority4());

        } else if (expr.skipIfMatch("l0")) {
            return new LeadingZeros(parsePriority4());

        } else if (expr.skipIfMatch("t0")) {
            return new TrailingZeros(parsePriority4());

        } else if (expr.skipIfMatch('-')) {
            if (!expr.hasNextChar()) {
                throw new ParseException("Unexpected end of stream, expected Const or any Expression after '-'");
            } else if (isDigit(expr.curChar())) {
                return new Const(parseInt(true));
            } else {
                return new CheckedNegate(parsePriority4());
            }

        } else if (isDigit(expr.curChar())) {
            return new Const(parseInt(false));

        } else if (VARIABLES.contains(expr.curChar())) {
            return new Variable(expr.getChar());

        } else {
            throw new ParseException(
                "Parser can not identify expression starting with char '" + expr.curChar() + "'"
            );
        }
    }

    private AbstractExpression parsePriority3() throws ParseException {
        AbstractExpression result = parsePriority4();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch("**")) {
                result = new CheckedPow(result, parsePriority4());
            } else if (expr.skipIfMatch("//")) {
                result = new CheckedLog(result, parsePriority4());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private AbstractExpression parsePriority2() throws ParseException {
        AbstractExpression result = parsePriority3();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('*')) {
                result = new CheckedMultiply(result, parsePriority3());
            } else if (expr.skipIfMatch('/')) {
                result = new CheckedDivide(result, parsePriority3());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private AbstractExpression parsePriority1() throws ParseException {
        AbstractExpression result = parsePriority2();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch('+')) {
                result = new CheckedAdd(result, parsePriority2());
            } else if (expr.skipIfMatch('-')) {
                result = new CheckedSubtract(result, parsePriority2());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    private AbstractExpression parsePriority0() throws ParseException {
        AbstractExpression result = parsePriority1();

        skipWhitespaces();
        while (expr.hasNextChar()) {
            if (expr.skipIfMatch(">>")) {
                if (expr.skipIfMatch('>')) {
                    result = new RightArithmeticBitshift(result, parsePriority1());
                } else {
                    result = new RightBitshift(result, parsePriority1());
                }
            } else if (expr.skipIfMatch("<<")) {
                result = new LeftBitshift(result, parsePriority1());
            } else {
                break;
            }
            skipWhitespaces();
        }

        return result;
    }

    @Override
    public AbstractExpression parse(String expression) throws ParseException {
        this.expr = new CheckedStringStreamer(expression);
        AbstractExpression result = parsePriority0();
        if (expr.hasNextChar()) {
            throw new ParseException("Expected end of stream after \"" + result + "\"");
        }
        return result;
    }
}
