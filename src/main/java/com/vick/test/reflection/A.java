package com.vick.test.reflection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class A extends Object implements ActionListener {
    private int a = 3;
    public Integer b = new Integer(4);

    public A() {
    }

    public A(int a, String name) {
    }

    public int abc(int id, String name) {
        return id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
