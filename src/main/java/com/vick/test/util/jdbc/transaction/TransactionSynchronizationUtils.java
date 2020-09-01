package com.vick.test.util.jdbc.transaction;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.aop.ScopedObject;
import com.vick.test.util.jdbc.framework.ClassUtils;
import com.vick.test.util.jdbc.framework.InfrastructureProxy;
import com.vick.test.util.jdbc.framework.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public abstract class TransactionSynchronizationUtils {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSynchronizationUtils.class);
    private static final boolean aopAvailable = ClassUtils.isPresent("org.springframework.aop.scope.ScopedObject", TransactionSynchronizationUtils.class.getClassLoader());

    public TransactionSynchronizationUtils() {
    }

    public static boolean sameResourceFactory(ResourceTransactionManager tm, Object resourceFactory) {
        return unwrapResourceIfNecessary(tm.getResourceFactory()).equals(unwrapResourceIfNecessary(resourceFactory));
    }

    static Object unwrapResourceIfNecessary(Object resource) {
        Assert.notNull(resource, "Resource must not be null");
        Object resourceRef = resource;
        if (resource instanceof InfrastructureProxy) {
            resourceRef = ((InfrastructureProxy)resource).getWrappedObject();
        }

        if (aopAvailable) {
            resourceRef = TransactionSynchronizationUtils.ScopedProxyUnwrapper.unwrapIfNecessary(resourceRef);
        }

        return resourceRef;
    }

    public static void triggerFlush() {
        Iterator var0 = TransactionSynchronizationManager.getSynchronizations().iterator();

        while(var0.hasNext()) {
            TransactionSynchronization synchronization = (TransactionSynchronization)var0.next();
            synchronization.flush();
        }

    }

    public static void triggerBeforeCommit(boolean readOnly) {
        Iterator var1 = TransactionSynchronizationManager.getSynchronizations().iterator();

        while(var1.hasNext()) {
            TransactionSynchronization synchronization = (TransactionSynchronization)var1.next();
            synchronization.beforeCommit(readOnly);
        }

    }

    public static void triggerBeforeCompletion() {
        Iterator var0 = TransactionSynchronizationManager.getSynchronizations().iterator();

        while(var0.hasNext()) {
            TransactionSynchronization synchronization = (TransactionSynchronization)var0.next();

            try {
                synchronization.beforeCompletion();
            } catch (Throwable var3) {
                logger.error("TransactionSynchronization.beforeCompletion threw exception", var3);
            }
        }

    }

    public static void triggerAfterCommit() {
        invokeAfterCommit(TransactionSynchronizationManager.getSynchronizations());
    }

    public static void invokeAfterCommit(@Nullable List<TransactionSynchronization> synchronizations) {
        if (synchronizations != null) {
            Iterator var1 = synchronizations.iterator();

            while(var1.hasNext()) {
                TransactionSynchronization synchronization = (TransactionSynchronization)var1.next();
                synchronization.afterCommit();
            }
        }

    }

    public static void triggerAfterCompletion(int completionStatus) {
        List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
        invokeAfterCompletion(synchronizations, completionStatus);
    }

    public static void invokeAfterCompletion(@Nullable List<TransactionSynchronization> synchronizations, int completionStatus) {
        if (synchronizations != null) {
            Iterator var2 = synchronizations.iterator();

            while(var2.hasNext()) {
                TransactionSynchronization synchronization = (TransactionSynchronization)var2.next();

                try {
                    synchronization.afterCompletion(completionStatus);
                } catch (Throwable var5) {
                    logger.error("TransactionSynchronization.afterCompletion threw exception", var5);
                }
            }
        }

    }

    private static class ScopedProxyUnwrapper {
        private ScopedProxyUnwrapper() {
        }

        public static Object unwrapIfNecessary(Object resource) {
            return resource instanceof ScopedObject ? ((ScopedObject)resource).getTargetObject() : resource;
        }
    }
}
