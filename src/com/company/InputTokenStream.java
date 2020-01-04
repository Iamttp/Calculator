package com.company;

import java.io.IOException;
import java.io.InputStream;

/*
    引入一个可以把字节流转成Token流的适配器
    即执行词法分析
 */

public class InputTokenStream {
    byte[] buf;
    int n = 0;
    public int pos = 0;

    public InputTokenStream(byte[] buf) {
        this.buf = buf;
        n = buf.length;
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
        while (pos < n && buf[pos] == ' ')
            pos++;
    }
}
