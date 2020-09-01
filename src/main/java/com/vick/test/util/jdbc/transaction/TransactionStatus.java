package com.vick.test.util.jdbc.transaction;

import java.io.Flushable;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface TransactionStatus extends TransactionExecution, SavepointManager, Flushable {
    boolean hasSavepoint();

    void flush();
}
