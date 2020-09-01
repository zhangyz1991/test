package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public interface NoOp extends Callback {
    NoOp INSTANCE = new NoOp() {
    };
}
