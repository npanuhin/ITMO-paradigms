package expression.parser;

public class StringStreamer implements AbstractStreamer {
    protected final String content;
    protected int pos = 0;

    public StringStreamer(String content) {
        this.content = content;
    }

    @Override
    public char curChar() {
        return content.charAt(pos);
    }

    @Override
    public char getChar() {
        return content.charAt(pos++);
    }

    @Override
    public void expect(final char c) {
        if (getChar() != c) {
            throw new IllegalArgumentException("Expected \"" + c + "\", got smth else");
        }
    }

    @Override
    public boolean hasNextChar() {
        return pos < content.length();
    }

    @Override
    public boolean skipIfMatch(final char c) {
        if (hasNextChar() && content.charAt(pos) == c) {
            pos++;
            return true;
        }
        return false;
    }

    @Override
    public boolean skipIfMatch(final String s) {
        if (content.length() < pos + s.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (content.charAt(pos + i) != s.charAt(i)) {
                return false;
            }
        }
        pos += s.length();
        return true;
    }

    // @Override
    // public String substring() {
    //     return content.substring(pos);
    // }
}
