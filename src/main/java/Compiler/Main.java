package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Queue<Token> q = Parser.parse(new File("/Users/s203295/IdeaProjects/ProLang/src/main/arith.lang"));
        System.out.println(q);
        Node entry = Lexer.parse(q);
        System.out.println("Break Here");
    }

}
