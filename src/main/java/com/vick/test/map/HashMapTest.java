package com.vick.test.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vick
 * @date 2020/9/22
 */
public class HashMapTest {
    public static void main(String[] args) {
        expandCapacity();
    }

    public static void expandCapacity() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        //Map<Integer, Integer> map = new HashMap<>((int)Math.ceil(list.size()/0.75));
        Map<Integer, Integer> map = new HashMap<>(4);
        //list.forEach(integer -> map.put(integer, integer));
        for (Integer integer : list) {
            map.put(integer, integer);
        }
    }
}
