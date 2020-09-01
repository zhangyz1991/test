package com.vick.test.util.jdbc.convert.converter;

import java.util.Comparator;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class ComparableComparator<T extends Comparable<T>> implements Comparator<T> {

    /**
     * A shared instance of this default comparator.
     * @see Comparators#comparable()
     */
    @SuppressWarnings("rawtypes")
    public static final ComparableComparator INSTANCE = new ComparableComparator();


    @Override
    public int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }

}
