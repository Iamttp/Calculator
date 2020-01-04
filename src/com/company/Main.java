package com.company;

import java.io.*;

/**
 * +-------+                      +--------+
 * -- source code --> | lexer | --> token stream --> | parser | --> assembly
 * +-------+                      +--------+
 */

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("please input");
        TokenStream ts = new InputTokenStream(new BufferedInputStream(System.in)); // 标准输入的适配器

        // 每次getToken都会返回当前游标所指向的那个token，
        // 每次consumeToken都会使得游标向后移动一位。
        Token token = ts.getToken();
        while (token.tokenType != Token.TokenType.NONE) {
            ts.consumeToken();
            System.out.println(token);
            token = ts.getToken();
        }
    }
}