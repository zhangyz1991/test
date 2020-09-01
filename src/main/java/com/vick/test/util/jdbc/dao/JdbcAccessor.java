package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public abstract class JdbcAccessor {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Nullable
    private DataSource dataSource;
    @Nullable
    private volatile SQLExceptionTranslator exceptionTranslator;
    private boolean lazyInit = true;

    public JdbcAccessor() {
    }

    public void setDataSource(@Nullable DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    public DataSource getDataSource() {
        return this.dataSource;
    }

    protected DataSource obtainDataSource() {
        DataSource dataSource = this.getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        return dataSource;
    }

    public void setDatabaseProductName(String dbName) {
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dbName);
    }

    public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    public SQLExceptionTranslator getExceptionTranslator() {
        SQLExceptionTranslator exceptionTranslator = this.exceptionTranslator;
        if (exceptionTranslator != null) {
            return exceptionTranslator;
        } else {
            synchronized(this) {
                exceptionTranslator = this.exceptionTranslator;
                if (exceptionTranslator == null) {
                    DataSource dataSource = this.getDataSource();
                    if (dataSource != null) {
                        exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
                    } else {
                        exceptionTranslator = new SQLStateSQLExceptionTranslator();
                    }

                    this.exceptionTranslator = (SQLExceptionTranslator)exceptionTranslator;
                }

                return (SQLExceptionTranslator)exceptionTranslator;
            }
        }
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }
}

