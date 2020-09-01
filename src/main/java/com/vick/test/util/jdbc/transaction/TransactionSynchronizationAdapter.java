package com.vick.test.util.jdbc.transaction;

import com.vick.test.util.jdbc.framework.Ordered;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public abstract class TransactionSynchronizationAdapter implements TransactionSynchronization, Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void beforeCommit(boolean readOnly) {
    }

    @Override
    public void beforeCompletion() {
    }

    @Override
    public void afterCommit() {
    }

    @Override
    public void afterCompletion(int status) {
    }

}
