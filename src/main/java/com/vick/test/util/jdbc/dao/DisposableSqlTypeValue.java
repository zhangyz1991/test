package com.vick.test.util.jdbc.dao;

public interface DisposableSqlTypeValue extends SqlTypeValue {
    void cleanup();
}
