package com.vick.test.compiler;

import java.io.IOException;

class Parser {
    static int lookahead;

    {
        System.out.println("block execute!");
    }

    static {
        System.out.println("static block execute!");
    }

    public Parser() throws IOException {
        System.out.println("Parser construct start!");
        lookahead = System.in.read();
        System.out.println("Parser construct end!");
    }

    void expr() throws IOException {
        term();
        while (true) {
            if (lookahead == '+') {
                match('+');
                term();
                System.out.write('+');
            } else if (lookahead == '-') {
                match('-');
                term();
                System.out.write('-');
            } else return;
        }
    }

    void term() throws IOException {
        if (Character.isDigit((char) lookahead)) {
            System.out.write((char) lookahead);
            match(lookahead);
        } else throw new Error("systax error");
    }

    void match(int t) throws IOException {
        if (lookahead == t)
            lookahead = System.in.read();
        else
            throw new Error("syntax error");
    }
}

public class Postfix {
    public static void main(String[] args) throws IOException {
        Parser parse = new Parser();
        parse.expr();
        System.out.write('\n');
    }
}