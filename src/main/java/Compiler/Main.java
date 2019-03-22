package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Queue<Token> q = Parser.parse(new File("/Users/s203295/IdeaProjects/ProLang/src/main/hello.lang"));
        Node entry = Lexer.parse(q);
        Translator.translate(entry, new File("/Users/s203295/IdeaProjects/ProLang/src/main/test.asm"));
        System.out.println("Done!");
    }

}
