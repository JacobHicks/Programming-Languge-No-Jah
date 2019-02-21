import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    static private String buffer;

    /* LIST OF ALL TOKENS */

    final static private Pattern whitespace = Pattern.compile("\\s+");
    final static private Pattern identifier = Pattern.compile("^[\\D]\\w+");
    final static private Pattern number = Pattern.compile("\\D{0}\\d+\\D{0}");
    final static private Pattern binary = Pattern.compile("\\D{0}[01]+b\\D{0}");
    final static private Pattern hexadecmal = Pattern.compile("\\D{0}0x[\\da-f[A-F]]+\\D{0}");
    final static private Pattern string = Pattern.compile("\".+\"");
    final static private Pattern openbrace = Pattern.compile("\\{");
    final static private Pattern closebrace = Pattern.compile("}");
    final static private Pattern openparenthesis = Pattern.compile("\\(");
    final static private Pattern closeparenthesis = Pattern.compile("\\)");
    final static private Pattern operator = Pattern.compile("[!@#$%^&*-+=<>.|/]+");
    final static private Pattern singlelinecomment = Pattern.compile("//.+\n*");
    final static private Pattern multilinecomment = Pattern.compile("/\\*[\\s\\S]*\\*/");



    public static Queue<Token> parse(File file) throws FileNotFoundException {
        Scanner in = new Scanner(file);
        in.useDelimiter(";");
        Queue<Token> tokens = new LinkedList<>();
        buffer = "";
        while(in.hasNext()) {
            if(buffer.length() == 0) {
                buffer = in.next();
            }
            if (singlelinecomment.matcher(buffer).(0)) {
                buffer = buffer.substring(singlelinecomment.matcher(buffer).end());
            }
            else if (multilinecomment.matcher(buffer).find(0)) {
                buffer = buffer.substring(multilinecomment.matcher(buffer).end());
            }
            else if (number.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, number.matcher(buffer).end()), Type.DECIMAL, buffer.substring(0, number.matcher(buffer).end())));
                buffer = buffer.substring(number.matcher(buffer).end());
            }
            else if(binary.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, binary.matcher(buffer).end()), Type.BINARY, buffer.substring(0, binary.matcher(buffer).end())));
                buffer = buffer.substring(binary.matcher(buffer).end());
            }
            else if(hexadecmal.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, hexadecmal.matcher(buffer).end()), Type.HEXADECIMAL, buffer.substring(0, hexadecmal.matcher(buffer).end())));
                buffer = buffer.substring(hexadecmal.matcher(buffer).end());
            }
            else if(string.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, string.matcher(buffer).end()), Type.STRING, buffer.substring(0, string.matcher(buffer).end())));
                buffer = buffer.substring(string.matcher(buffer).end());
            }
            else if(openbrace.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, openbrace.matcher(buffer).end()), Type.VOID, null));
                buffer = buffer.substring(openbrace.matcher(buffer).end());
            }
            else if(closebrace.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, closebrace.matcher(buffer).end()), Type.VOID, null));
                buffer = buffer.substring(closebrace.matcher(buffer).end());
            }
            else if(openparenthesis.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, openparenthesis.matcher(buffer).end()), Type.VOID, null));
                buffer = buffer.substring(openparenthesis.matcher(buffer).end());
            }
            else if(closeparenthesis.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, closeparenthesis.matcher(buffer).end()), Type.VOID, null));
                buffer = buffer.substring(closeparenthesis.matcher(buffer).end());
            }
            else if(operator.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, operator.matcher(buffer).end()), Type.VOID, null));
                buffer = buffer.substring(operator.matcher(buffer).end());
            }
            else if (identifier.matcher(buffer).find(0)) {
                tokens.offer(new Token(buffer.substring(0, identifier.matcher(buffer).end()), Type.VOID, null));
                buffer = buffer.substring(identifier.matcher(buffer).end());
            }
            else if (whitespace.matcher(buffer).find(0)) {
                buffer = buffer.substring(whitespace.matcher(buffer).end());
            }
        }
        in.close();
        return tokens;
    }
}
