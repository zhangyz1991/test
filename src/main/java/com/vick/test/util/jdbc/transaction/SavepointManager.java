package com.vick.test.util.jdbc.transaction;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface SavepointManager {
    Object createSavepoint() throws TransactionException;

    void rollbackToSavepoint(Object var1) throws TransactionException;

    void releaseSavepoint(Object var1) throws TransactionException;
}
