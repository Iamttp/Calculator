package com.company;

/*
    词法分析需要的Token
 */
public class Token {
    public enum TokenType {
        LPAR, RPAR,
        PLUS,
        MINUS,
        MULT,
        DIV,
        INT,
        NONE,
        BLOCK,
    }

    public TokenType tokenType;
    public Object value;

    public Token(TokenType tt, Object v) {
        this.tokenType = tt;
        this.value = v;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value=" + value +
                '}';
    }
}


