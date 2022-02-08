package myclasses;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.InputStream;
import java.io.Reader;

import java.io.IOException;


public class Scanner implements AutoCloseable {
    public interface Delimiter {
        public boolean isWhitespace(char c);
    }

    private static class DefaultDelimiter implements Delimiter {
        public boolean isWhitespace(char c) {
            return c == ' ';
        }
    }

    private Reader reader;
    private int read;
    private Delimiter delimiter = new DefaultDelimiter();

    private char[] buffer = new char[1024];  // 1024, 1MB = 1048576
    private int bufferPos = 0;
    private int storedInBuffer = 0;

    private boolean hasBufferedChar = false;
    private char bufferedChar;

    private char[] lineSeparators = new char[] {'\n', '\r', '\u000B', '\u000C', '\u0085', '\u2028', '\u2029'};


    public Scanner(InputStreamReader reader) {
        this.reader = reader;
    }
    public Scanner(InputStreamReader reader, Delimiter delimiter) {
        this.reader = reader;
        this.delimiter = delimiter;
    }
    public Scanner(InputStream in) throws IOException {
        this(new InputStreamReader(in, "utf-8"));
    }
    public Scanner(InputStream in, Delimiter delimiter) throws IOException {
        this(new InputStreamReader(in, "utf-8"), delimiter);
    }


    public Scanner(StringReader in) {
        this.reader = in;
    }
    public Scanner(StringReader in, Delimiter delimiter) {
        this.reader = in;
        this.delimiter = delimiter;
    }
    public Scanner(String in) throws IOException {
        this(new StringReader(in));
    }
    public Scanner(String in, Delimiter delimiter) throws IOException {
        this(new StringReader(in), delimiter);
    }



    public void close() throws IOException {
        reader.close();
    }

    // =============================================================================

    private boolean read() throws IOException {
        if (!hasBufferedChar) {
            while (bufferPos == storedInBuffer || storedInBuffer == -1) {
                if ((storedInBuffer = reader.read(buffer)) == -1) {
                    return false;
                }
                bufferPos = 0;
            }
            bufferedChar = buffer[bufferPos++];
            hasBufferedChar = true;
        }
        return true;
    }

    public boolean canRead() throws IOException {
        return hasBufferedChar || read();
    }

    // =============================================================================

    // private boolean isWhitespace() {
    //     return delimiter.isWhitespace(bufferedChar);
    // }

    private boolean isEndOfLine() {
        for (char testSeparator : lineSeparators) {
            if (testSeparator == bufferedChar) {
                return true;
            }
        }
        return false;
    }

    private boolean isDigit() {
        return Character.isDigit(bufferedChar);
    }

    private void skipWhitespaces() throws IOException {
        while (canRead() && delimiter.isWhitespace(bufferedChar) && !isEndOfLine()) {
            hasBufferedChar = false;
        }
    }

    // private void skipLinesAndWhitespaces() throws IOException {
    //     while (canRead() && (delimiter.isWhitespace(bufferedChar) || isEndOfLine())) {
    //         hasBufferedChar = false;
    //     }
    // }

    // =============================================================================

    public boolean hasNextInLine() throws IOException {
        skipWhitespaces();
        return hasBufferedChar && !isEndOfLine();
    }

    public void skipLine() throws IOException {
        while (canRead() && !isEndOfLine()) {
            hasBufferedChar = false;
        }
        hasBufferedChar = false;

        if (
            (bufferedChar == '\r' && canRead() && bufferedChar == '\n') || 
            (bufferedChar == '\n' && canRead() && bufferedChar == '\r')
        ) {
            hasBufferedChar = false;
        }
    }

    // =============================================================================

    public String nextString() throws IOException {
        skipWhitespaces();

        // if (isEndOfLine()) {
        //     throw new IOException("Empty input to generate String"); 
        // }

        StringBuilder result = new StringBuilder();

        while (canRead() && !delimiter.isWhitespace(bufferedChar) && !isEndOfLine()) {
            result.append(bufferedChar);
            hasBufferedChar = false;
        }

        return result.toString();
    }

    public int nextInt() throws IOException {
        String number = nextString();

        if (number.startsWith("0x") || number.startsWith("0X")) {
            return Integer.parseInt(number.substring(2), 16);
        } else {
            return Integer.parseInt(number);
        }
    }

    public String nextLine() throws IOException {
        // if (isEndOfLine()) {
        //     throw new IOException("Empty input to generate String"); 
        // }

        StringBuilder result = new StringBuilder();

        while (canRead() && !isEndOfLine()) {
            result.append(bufferedChar);
            hasBufferedChar = false;
        }

        return result.toString();
    }
}
