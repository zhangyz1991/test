package com.vick.test.util.jdbc.aop;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface ScopedObject extends RawTargetAccess {
    Object getTargetObject();

    void removeFromScope();
}
