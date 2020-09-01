package com.vick.test.util.jdbc.transaction;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
final class StaticTransactionDefinition implements TransactionDefinition {

    static final StaticTransactionDefinition INSTANCE = new StaticTransactionDefinition();

    private StaticTransactionDefinition() {
    }

}
