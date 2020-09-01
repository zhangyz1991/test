package com.vick.test.util.jdbc.transaction;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface TransactionExecution {
    boolean isNewTransaction();

    void setRollbackOnly();

    boolean isRollbackOnly();

    boolean isCompleted();
}
