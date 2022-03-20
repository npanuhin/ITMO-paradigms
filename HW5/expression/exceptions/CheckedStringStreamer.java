package expression.exceptions;

import expression.parser.StringStreamer;


public class CheckedStringStreamer extends StringStreamer {
    public CheckedStringStreamer(String content) {
        super(content);
    }

    @Override
    public void expect(final char c) {
        if (!hasNextChar() || getChar() != c) {
            throw new ParseException("Expected \"" + c + "\" on position " + pos + ", got smth else");
        }
    }
}
