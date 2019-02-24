import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Queue<Token> q = Parser.parse(new File("C:\\Users\\hicks\\IdeaProjects\\ProgrammingLang\\src\\main\\hello.lang"));
        System.out.println(q);
    }
}
