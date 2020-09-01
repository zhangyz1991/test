package com.vick.test.util.jdbc.transaction;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface ResourceHolder {

    /**
     * Reset the transactional state of this holder.
     */
    void reset();

    /**
     * Notify this holder that it has been unbound from transaction synchronization.
     */
    void unbound();

    /**
     * Determine whether this holder is considered as 'void',
     * i.e. as a leftover from a previous thread.
     */
    boolean isVoid();

}
