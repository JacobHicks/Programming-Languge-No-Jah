package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Queue<Token> q = Parser.parse(new File("C:\\Users\\hicks\\IdeaProjects\\ProLang\\src\\main\\arith.lang"));
        Node entry = Lexer.parse(q);

    }

}
