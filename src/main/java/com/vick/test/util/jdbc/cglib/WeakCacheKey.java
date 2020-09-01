package com.vick.test.util.jdbc.cglib;

import java.lang.ref.WeakReference;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class WeakCacheKey<T> extends WeakReference<T> {
    private final int hash;

    public WeakCacheKey(T referent) {
        super(referent);
        this.hash = referent.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WeakCacheKey)) {
            return false;
        } else {
            Object ours = this.get();
            Object theirs = ((WeakCacheKey)obj).get();
            return ours != null && theirs != null && ours.equals(theirs);
        }
    }

    public int hashCode() {
        return this.hash;
    }

    public String toString() {
        T t = this.get();
        return t == null ? "Clean WeakIdentityKey, hash: " + this.hash : t.toString();
    }
}
