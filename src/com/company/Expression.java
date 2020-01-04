package com.company;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/*
    递归下降方法计算表达式
    expr := term (+|-) term (+|-) ... (+|-) term
    term := factor (*|/) factor (* | /) ... (*|/) factor
    factor := INT | "(" expr ")"
 */


public class Expression {
    public InputTokenStream ts;

    public Expression(InputStream in) {
        ts = new InputTokenStream(new BufferedInputStream(in));
    }

    // expr := term (+|-) term (+|-) ... (+|-) term
    public int evalue(Stack<TreeNode> treeNodeList) throws IOException {
        treeNodeList.push(new TreeNode("term"));
        int t = term(treeNodeList.peek().child);
        Token op = ts.getToken();
        while (op.tokenType == Token.TokenType.PLUS || op.tokenType == Token.TokenType.MINUS) {
            ts.consumeToken();
            if (op.tokenType == Token.TokenType.PLUS) {
                treeNodeList.push(new TreeNode("+"));
            } else {
                treeNodeList.push(new TreeNode("-"));
            }
            treeNodeList.push(new TreeNode("term"));
            int t2 = term(treeNodeList.peek().child);
            if (op.tokenType == Token.TokenType.PLUS) {
                t += t2;
            } else {
                t -= t2;
            }
            op = ts.getToken();
        }
        return t;
    }

    // term := factor (*|/) factor (* | /) ... (*|/) factor
    private int term(Stack<TreeNode> treeNodeList) throws IOException {
        treeNodeList.push(new TreeNode("factor"));
        int t = factor(treeNodeList.peek().child);
        Token op = ts.getToken();
        while (op.tokenType == Token.TokenType.MULT || op.tokenType == Token.TokenType.DIV) {
            ts.consumeToken();
            if (op.tokenType == Token.TokenType.MULT) {
                treeNodeList.push(new TreeNode("*"));
            } else {
                treeNodeList.push(new TreeNode("/"));
            }
            treeNodeList.push(new TreeNode("factor"));
            int t2 = factor(treeNodeList.peek().child);
            if (op.tokenType == Token.TokenType.MULT) {
                t *= t2;
            } else {
                t /= t2;
            }
            op = ts.getToken();
        }
        return t;
    }

    // factor := INT | "(" expr ")"
    private int factor(Stack<TreeNode> treeNodeList) throws IOException {
        Token t = ts.getToken();
        if (t.tokenType == Token.TokenType.INT) {
            ts.consumeToken();
            treeNodeList.push(new TreeNode(t.value.toString()));
            return ((Integer) t.value);
        } else if (t.tokenType == Token.TokenType.LPAR) {
            ts.consumeToken();
            treeNodeList.push(new TreeNode("("));
            treeNodeList.push(new TreeNode("evalue"));
            int v = evalue(treeNodeList.peek().child);
            treeNodeList.push(new TreeNode(")"));
            match(ts.getToken(), Token.TokenType.RPAR);
            return v;
        } else if (t.tokenType == Token.TokenType.MINUS) {
            ts.consumeToken();
            treeNodeList.push(new TreeNode("-"));
            return -factor(treeNodeList.peek().child);
        } else {
            String error = "发生错误！" +
                    "\t错误token为\t" + t +
                    "\t错误发生位置\t" + ts.pos;
            throw new IOException(error);
        }
    }

    // expr := term (+|-) term (+|-) ... (+|-) term
    public int evalue() throws IOException {
        int t = term();
        Token op = ts.getToken();
        while (op.tokenType == Token.TokenType.PLUS || op.tokenType == Token.TokenType.MINUS) {
            ts.consumeToken();
            int t2 = term();
            if (op.tokenType == Token.TokenType.PLUS) {
                t += t2;
            } else {
                t -= t2;
            }
            op = ts.getToken();
        }
        return t;
    }

    // term := factor (*|/) factor (* | /) ... (*|/) factor
    private int term() throws IOException {
        int t = factor();
        Token op = ts.getToken();
        while (op.tokenType == Token.TokenType.MULT || op.tokenType == Token.TokenType.DIV) {
            ts.consumeToken();
            int t2 = factor();
            if (op.tokenType == Token.TokenType.MULT) {
                t *= t2;
            } else {
                t /= t2;
            }
            op = ts.getToken();
        }
        return t;
    }

    // factor := INT | "(" expr ")"
    private int factor() throws IOException {
        Token t = ts.getToken();
        if (t.tokenType == Token.TokenType.INT) {
            ts.consumeToken();
            return ((Integer) t.value);
        } else if (t.tokenType == Token.TokenType.LPAR) {
            ts.consumeToken();
            int v = evalue();
            match(ts.getToken(), Token.TokenType.RPAR);
            return v;
        } else if (t.tokenType == Token.TokenType.MINUS) {
            ts.consumeToken();
            return -factor();
        } else {
            String error = "发生错误！" +
                    "\t错误token为\t" + t +
                    "\t错误发生位置\t" + ts.pos;
            throw new IOException(error);
        }
    }

    private void match(Token t, Token.TokenType tt) {
        assert t.tokenType == tt;
        ts.consumeToken();
    }
}