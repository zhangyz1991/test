package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.NestedExceptionUtils;
import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public abstract class NestedCheckedException extends Exception {
    private static final long serialVersionUID = 7100714597678207546L;

    public NestedCheckedException(String msg) {
        super(msg);
    }

    public NestedCheckedException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Nullable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }

    @Nullable
    public Throwable getRootCause() {
        return NestedExceptionUtils.getRootCause(this);
    }

    public Throwable getMostSpecificCause() {
        Throwable rootCause = this.getRootCause();
        return (Throwable)(rootCause != null ? rootCause : this);
    }

    public boolean contains(@Nullable Class<?> exType) {
        if (exType == null) {
            return false;
        } else if (exType.isInstance(this)) {
            return true;
        } else {
            Throwable cause = this.getCause();
            if (cause == this) {
                return false;
            } else if (cause instanceof NestedCheckedException) {
                return ((NestedCheckedException)cause).contains(exType);
            } else {
                while(cause != null) {
                    if (exType.isInstance(cause)) {
                        return true;
                    }

                    if (cause.getCause() == cause) {
                        break;
                    }

                    cause = cause.getCause();
                }

                return false;
            }
        }
    }

    static {
        NestedExceptionUtils.class.getName();
    }
}
