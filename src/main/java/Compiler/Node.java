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

    public Node(Token token) {
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
}
