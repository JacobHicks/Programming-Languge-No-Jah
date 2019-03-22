package Compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    //ORDER OF OPERATIONS\\
    private final String[][] orderOfOperations = {
            {"."},
            {"*", "/"},
            {"+", "-"},
            {"=="},
            {"="},
            {">>", "<<"},
            {"entry"}

    };
    ///////////////////////
    private Node parent;
    private ArrayList<Node> children;
    private Token token;
    private int precedence;
    private boolean isOperator;

    public String asm;
    public String outputregister;

    public Node(Token token) {
        update(token);
    }

    public void update(Token token) {
        this.token = token;
        children = new ArrayList<>();
        isOperator = false;

        labelsarebad:
        for(int i = 0; i < orderOfOperations.length; i++) {
            for(int j = 0; j < orderOfOperations[i].length; j++) {
                if(orderOfOperations[i][j].equals(token.identifier)) {
                    isOperator = true;
                    precedence = i;
                    break labelsarebad;
                }
            }
        }
        asm = setTemplate(token);
    }

    private String setTemplate(Token token) {
        if(token.type.equals(Type.VOID)) {
            switch (token.identifier) {
                case("int"):
                    outputregister = "{$}";
                    return "[1]" +
                            "DW {$}" +
                            "[0]\n";
                case ("+"):
                    outputregister = "eax ebx";
                    return "mov eax, <1>\n" +
                            "mov ebx, <2>\n" +
                            "add eax, ebx\n" +
                            "mov <1>, eax\n";
                case ("="):
                    outputregister = "<1>";
                    return "mov <1>, <2>\n";
                case ("print"):
                    outputregister = "null";
                    return "mov ecx, <1>\n" +
                            "push ecx\n" +
                            "call msvcrt.puts\n";
            }
        }
        outputregister = token.identifier;
        return "";
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node... children) {
        this.children.addAll(Arrays.asList(children));
    }

    public boolean isOperator() {
        return isOperator;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isTerminator() {
        return token.identifier.equals(";");
    }
    public boolean equals(String s) {
        return s.equals(token.identifier);
    }
    public boolean matches(String s) {
        return token.identifier.matches(s);
    }
    public Type getType() {
        return token.type;
    }
    public Object getValue() {
        return token.value;
    }
    public ArrayList<Node> getChildren() {
        return children;
    }
}
