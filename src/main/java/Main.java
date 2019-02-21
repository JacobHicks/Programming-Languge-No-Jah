import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Queue<Token> q = Parser.parse(new File("/Users/s203295/IdeaProjects/ProLang/src/main/fibonacci.lang"));
        System.out.println(q);
    }
}
