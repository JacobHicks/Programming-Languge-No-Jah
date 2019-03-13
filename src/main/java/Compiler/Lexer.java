package Compiler;

import java.util.Queue;
import java.util.Stack;

public class Lexer {
    static Node entry;
    public static Node parse(Queue<Token> inputQ) {
        Token entrytoken = inputQ.poll();
        while(!entrytoken.identifier.equals("entry")) {
            inputQ.offer(entrytoken);
            entrytoken = inputQ.poll();
        }
        entry = new Node(entrytoken);
        Stack<Node> operators = new Stack<>();
        Stack<Node> operands = new Stack<>();
        //Stack<Node> statements = new Stack<>();
        while(!inputQ.isEmpty()) {
            Node node = new Node(inputQ.poll());
            if(node.isOperator()) {
                while (!operators.isEmpty() && operators.peek().getPrecedence() <= node.getPrecedence()) {
                    Node operator = operators.pop();
                    Node exp2 = operands.pop();
                    Node exp1 = operands.pop();
                    exp2.setParent(operator);
                    exp1.setParent(operator);
                    operator.addChild(exp1, exp2);
                    operands.push(operator);
                }
                operators.push(node);
            }
            else if(node.isTerminator()) {
                while (!operators.isEmpty()) {
                    Node operator = operators.pop();
                    Node exp2 = operands.pop();
                    Node exp1 = operands.pop();
                    exp2.setParent(operator);
                    exp1.setParent(operator);
                    operator.addChild(exp1, exp2);
                    operands.push(operator);
                }
                entry.addChild(operands.pop());
            }
            else {
                operands.push(node);
            }
        }
        return entry;
    }
}