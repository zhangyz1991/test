package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ObjectUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringJoiner;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public class PropertyBatchUpdateException extends BeansException {

    /** List of PropertyAccessException objects. */
    private final PropertyAccessException[] propertyAccessExceptions;


    /**
     * Create a new PropertyBatchUpdateException.
     * @param propertyAccessExceptions the List of PropertyAccessExceptions
     */
    public PropertyBatchUpdateException(PropertyAccessException[] propertyAccessExceptions) {
        super(null, null);
        Assert.notEmpty(propertyAccessExceptions, "At least 1 PropertyAccessException required");
        this.propertyAccessExceptions = propertyAccessExceptions;
    }


    /**
     * If this returns 0, no errors were encountered during binding.
     */
    public final int getExceptionCount() {
        return this.propertyAccessExceptions.length;
    }

    /**
     * Return an array of the propertyAccessExceptions stored in this object.
     * <p>Will return the empty array (not {@code null}) if there were no errors.
     */
    public final PropertyAccessException[] getPropertyAccessExceptions() {
        return this.propertyAccessExceptions;
    }

    /**
     * Return the exception for this field, or {@code null} if there isn't any.
     */
    @Nullable
    public PropertyAccessException getPropertyAccessException(String propertyName) {
        for (PropertyAccessException pae : this.propertyAccessExceptions) {
            if (ObjectUtils.nullSafeEquals(propertyName, pae.getPropertyName())) {
                return pae;
            }
        }
        return null;
    }


    @Override
    public String getMessage() {
        StringJoiner stringJoiner = new StringJoiner("; ", "Failed properties: ", "");
        for (PropertyAccessException exception : this.propertyAccessExceptions) {
            stringJoiner.add(exception.getMessage());
        }
        return stringJoiner.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append("; nested PropertyAccessExceptions (");
        sb.append(getExceptionCount()).append(") are:");
        for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
            sb.append('\n').append("PropertyAccessException ").append(i + 1).append(": ");
            sb.append(this.propertyAccessExceptions[i]);
        }
        return sb.toString();
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            ps.println(getClass().getName() + "; nested PropertyAccessException details (" +
                    getExceptionCount() + ") are:");
            for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
                ps.println("PropertyAccessException " + (i + 1) + ":");
                this.propertyAccessExceptions[i].printStackTrace(ps);
            }
        }
    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            pw.println(getClass().getName() + "; nested PropertyAccessException details (" +
                    getExceptionCount() + ") are:");
            for (int i = 0; i < this.propertyAccessExceptions.length; i++) {
                pw.println("PropertyAccessException " + (i + 1) + ":");
                this.propertyAccessExceptions[i].printStackTrace(pw);
            }
        }
    }

    @Override
    public boolean contains(@Nullable Class<?> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        for (PropertyAccessException pae : this.propertyAccessExceptions) {
            if (pae.contains(exType)) {
                return true;
            }
        }
        return false;
    }

}
