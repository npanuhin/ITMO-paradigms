package expression.parser;

public interface AbstractStreamer {
    char curChar();
    char getChar();
    boolean hasNextChar();

    boolean skipIfMatch(char c);
    boolean skipIfMatch(String c);

    void expect(char c);
    // default void expect(String s) {
    //     for (int i = 0; i < s.length(); ++i) {
    //         expect(s.charAt(i));
    //     }
    // }

    // String substring();
}
