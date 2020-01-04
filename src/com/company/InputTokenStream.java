package com.company;

import java.io.IOException;
import java.io.InputStream;

/*
    引入一个可以把字节流转成Token流的适配器
    即执行词法分析
 */

public class InputTokenStream {
    byte[] buf = new byte[512];
    int n = 0;
    public int pos = 0;

    public InputTokenStream(InputStream in) {
        try {
            n = in.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Token getToken() throws IOException {
        if (pos >= n) return new Token(Token.TokenType.NONE, null);

        if (buf[pos] == '(')
            return new Token(Token.TokenType.LPAR, "(");
        else if (buf[pos] == ')')
            return new Token(Token.TokenType.RPAR, ")");
        else if (buf[pos] == '+')
            return new Token(Token.TokenType.PLUS, "+");
        else if (buf[pos] == '-')
            return new Token(Token.TokenType.MINUS, "-");
        else if (buf[pos] == '*')
            return new Token(Token.TokenType.MULT, "*");
        else if (buf[pos] == '/')
            return new Token(Token.TokenType.DIV, "/");
        else if (buf[pos] >= '0' && buf[pos] <= '9') {
            int val = 0;
            while (buf[pos] >= '0' && buf[pos] <= '9') {
                val = val * 10 + buf[pos++] - '0';
            }
            pos--;
            return new Token(Token.TokenType.INT, val);
        } else
            return new Token(Token.TokenType.BLOCK, " ");
    }

    public void consumeToken() {
        pos++;
        while (buf[pos] == ' ')
            pos++;
    }
}
