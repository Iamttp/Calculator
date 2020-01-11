package com.company;

import java.io.IOException;

/*
    引入一个可以把字节流转成Token流的适配器
    即执行词法分析
 */
public class InputTokenStream {
    byte[] buf;
    int n = 0;
    public int pos = 0;
    Token last_token = null;

    public InputTokenStream(byte[] buf) {
        this.buf = buf;
        n = buf.length;
    }

    private Token pushToken(Token.TokenType tt, Object v) {
        last_token = new Token(tt, v);
        return last_token;
    }

    public Token getToken() {
        if (pos >= n) return pushToken(Token.TokenType.NONE, null);

        if (buf[pos] == '(')
            return pushToken(Token.TokenType.LPAR, "(");
        else if (buf[pos] == ')')
            return pushToken(Token.TokenType.RPAR, ")");
        else if (buf[pos] == '+')
            return pushToken(Token.TokenType.PLUS, "+");
        else if (buf[pos] == '-' && last_token != null && last_token.tokenType != Token.TokenType.LPAR)
            return pushToken(Token.TokenType.MINUS, "-");
        else if (buf[pos] == '*')
            return pushToken(Token.TokenType.MULT, "*");
        else if (buf[pos] == '/')
            return pushToken(Token.TokenType.DIV, "/");
        else if (buf[pos] >= '0' && buf[pos] <= '9') {
            int val = 0;
            while (pos < n && buf[pos] >= '0' && buf[pos] <= '9') {
                val = val * 10 + buf[pos++] - '0';
            }
            pos--;
            return pushToken(Token.TokenType.INT, val);
        } else if (buf[pos] == '-') {   // 负数识别
            pos++;
            int val = 0;
            while (pos < n && buf[pos] >= '0' && buf[pos] <= '9') {
                val = val * 10 + buf[pos++] - '0';
            }
            pos--;
            return pushToken(Token.TokenType.INT, -val);
        } else
            return pushToken(Token.TokenType.BLOCK, " ");
    }

    public void consumeToken() {
        pos++;
        while (pos < n && buf[pos] == ' ')
            pos++;
    }
}
