package com.vick.test.util.jdbc.cglib;

import java.lang.reflect.Member;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class RejectModifierPredicate implements Predicate {
    private int rejectMask;

    public RejectModifierPredicate(int rejectMask) {
        this.rejectMask = rejectMask;
    }

    public boolean evaluate(Object arg) {
        return (((Member)arg).getModifiers() & this.rejectMask) == 0;
    }
}
