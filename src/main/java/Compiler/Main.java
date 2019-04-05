package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Queue<Token> q = Parser.parse(new File("C:\\Users\\beverly\\IdeaProjects\\ProgrammingLang\\src\\main\\fibonacci.lang"));
        Node entry = ASTFactory.parse(q);
        Translator.translate(entry, new File("C:\\Users\\beverly\\IdeaProjects\\ProgrammingLang\\test.asm"));
        System.out.println("Done!");
    }

}
