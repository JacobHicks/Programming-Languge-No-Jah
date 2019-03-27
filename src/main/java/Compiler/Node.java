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
    int labelcount = 0;
    int numstrings = 0;

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
                case ("+"):
                    outputregister = "eax";
                    return "mov eax, <1>\n" +
                            "mov ebx, <2>\n" +
                            "add eax, ebx\n";
                case ("-"):
                    outputregister = "eax";
                    return "mov eax, <1>\n" +
                            "mov ebx, <2>\n" +
                            "sub eax, ebx\n";
                case ("*"):
                    outputregister = "eax";
                    return "mov eax, <1>\n" +
                            "mov ebx, <2>\n" +
                            "mul ebx\n";
                case ("/"):
                    outputregister = "eax";
                    return "mov eax, <1>\n" +
                            "mov ebx, <2>\n" +
                            "div eax, ebx\n";
                case ("="):
                    outputregister = "<1>";
                    return "mov `<1>, <2>\n";
                case("if"):
                    labelcount++;
                    return "j<1> label" + labelcount + "\n";
                case ("print"):
                    outputregister = "null";
                    return "push <1>\n" +
                            "call _printf\n" +
                            "add esp, 4\n";
            }
        }
        else if(token.type.equals(Type.STRING)) {
            if(token.value.toString().startsWith("\"") && token.value.toString().endsWith("\"")) {
                outputregister = "str" + numstrings++;
                return "[2]" +
                        outputregister + ": db " + token.value.toString() + ", 0\n" +
                        "[0]\n";
            }
            switch (token.identifier) {
                case("int"):
                    outputregister = "{$}";
                    return "[1]" +
                            "{$}: resd 0\n" +
                            "[0]\n";
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
