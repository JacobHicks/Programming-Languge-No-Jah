package Compiler;

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
    final static private Pattern entryidentifier = Pattern.compile("(entry)\\s+([^\\d&&[^\\s]][\\w&&[^\\s]]*)");
    final static private Pattern identifier = Pattern.compile("[^\\d&&[^\\s]][\\w&&[^\\s]]*");
    final static private Pattern number = Pattern.compile("\\D{0}\\d+\\D{0}");
    final static private Pattern binary = Pattern.compile("\\D{0}[01]+b\\D{0}");
    final static private Pattern hexadecmal = Pattern.compile("\\D{0}0x[\\da-f[A-F]]+\\D{0}");
    final static private Pattern string = Pattern.compile("\"(.+)\"");
    final static private Pattern openbrace = Pattern.compile("\\{");
    final static private Pattern closebrace = Pattern.compile("}");
    final static private Pattern openparenthesis = Pattern.compile("\\(");
    final static private Pattern closeparenthesis = Pattern.compile("\\)");
    final static private Pattern operator = Pattern.compile("[!@#$%^&*-+=<>.|/]+");
    final static private Pattern singlelinecomment = Pattern.compile("//.+\n*");
    final static private Pattern multilinecomment = Pattern.compile("/\\*[\\s\\S]*\\*/");

    public static Queue<Token> parse(File file) throws FileNotFoundException {
        Scanner in = new Scanner(file);
        in.useDelimiter(Pattern.compile("\\z"));
        Queue<Token> tokens = new LinkedList<>();
        buffer = in.next();
        while(buffer.length() != 0) {

            Matcher singleline = singlelinecomment.matcher(buffer); //All the matchers, there has to be a better way
            Matcher multi = multilinecomment.matcher(buffer);
            Matcher bin = binary.matcher(buffer);
            Matcher num = number.matcher(buffer);
            Matcher hex = hexadecmal.matcher(buffer);
            Matcher str = string.matcher(buffer);
            Matcher openbr = openbrace.matcher(buffer);
            Matcher closebr = closebrace.matcher(buffer);
            Matcher openpar = openparenthesis.matcher(buffer);
            Matcher closepar = closeparenthesis.matcher(buffer);
            Matcher oper = operator.matcher(buffer);
            Matcher entryidenti = entryidentifier.matcher(buffer);
            Matcher identi = identifier.matcher(buffer);
            Matcher whites = whitespace.matcher(buffer);

            if (singleline.lookingAt()) {
                buffer = buffer.substring(singleline.end());
            }
            else if (multi.lookingAt()) {
                buffer = buffer.substring(multi.end());
            }
            else if (num.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, num.end()), Type.DECIMAL, buffer.substring(0, num.end())));
                buffer = buffer.substring(num.end());
            }
            else if(bin.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, bin.end()), Type.BINARY, buffer.substring(0, bin.end())));
                buffer = buffer.substring(bin.end());
            }
            else if(hex.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, hex.end()), Type.HEXADECIMAL, buffer.substring(0, hex.end())));
                buffer = buffer.substring(hex.end());
            }
            else if(str.lookingAt()) {
                tokens.offer(new Token(str.group(0), Type.STRING, str.group(1)));
                buffer = buffer.substring(str.end());
            }
            else if(openbr.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, openbr.end()), Type.VOID, null));
                buffer = buffer.substring(openbr.end());
            }
            else if(closebr.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, closebr.end()), Type.VOID, null));
                buffer = buffer.substring(closebr.end());
            }
            else if(openpar.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, openpar.end()), Type.VOID, null));
                buffer = buffer.substring(openpar.end());
            }
            else if(closepar.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, closepar.end()), Type.VOID, null));
                buffer = buffer.substring(closepar.end());
            }
            else if(oper.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, oper.end()), Type.VOID, null));
                buffer = buffer.substring(oper.end());
            }
            else if (entryidenti.lookingAt()) {
                tokens.offer(new Token(entryidenti.group(1), Type.STRING, entryidenti.group(2)));
                buffer = buffer.substring(entryidenti.end());
            }
            else if (identi.lookingAt()) {
                tokens.offer(new Token(buffer.substring(0, identi.end()), Type.VOID, null));
                buffer = buffer.substring(identi.end());
            }
            else if (whites.lookingAt()) {
                buffer = buffer.substring(whites.end());
            }
        }
        in.close();
        return tokens;
    }
}
