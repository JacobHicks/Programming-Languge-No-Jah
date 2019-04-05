package Compiler;

import java.util.Queue;
import java.util.Stack;

public class ASTFactory {
    static Node entry;
    public static Node parse(Queue<Token> inputQ) {
        Token entrytoken = inputQ.poll();
        while(!entrytoken.identifier.equals("entry")) {
            inputQ.offer(entrytoken);
            entrytoken = inputQ.poll();
        }
        Stack<Node> operators = new Stack<>();
        Stack<Node> operands = new Stack<>();
        Stack<Node> branched = new Stack<>();
        operands.push(new Node(entrytoken));
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
            else if(node.matches("[({]")) {
                branched.push(operands.pop());
            }
            else if(node.matches("[)}]")) {
                Node branch = branched.pop();
                if(!branched.isEmpty() && !node.equals(")")) {
                    branched.peek().addChild(branch);
                }
                else {
                    operands.push(branch);
                }
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
                branched.peek().addChild(operands.pop());
            }
            else {
                operands.push(node);
            }
        }
        return operands.pop();
    }
}