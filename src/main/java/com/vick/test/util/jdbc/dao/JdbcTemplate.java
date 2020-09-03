package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;
import com.vick.test.util.jdbc.framework.Assert;

import java.sql.*;
import java.util.List;

/**
 * @author Vick Zhang
 * @date 2020/8/22
 */
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations{

    /**
     * If this variable is set to a non-negative value, it will be used for setting the
     * fetchSize property on statements used for query processing.
     */
    private int fetchSize = -1;
    /**
     * If this variable is set to a non-negative value, it will be used for setting the
     * maxRows property on statements used for query processing.
     */
    private int maxRows = -1;
    /**
     * If this variable is set to a non-negative value, it will be used for setting the
     * queryTimeout property on statements used for query processing.
     */
    private int queryTimeout = -1;
    /** If this variable is false, we will throw exceptions on SQL warnings. */
    private boolean ignoreWarnings = true;

    public Connection getConnection(String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    @Override
    public <T> List<T> query(String sql, @Nullable Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return result(query(sql, args, new RowMapperResultSetExtractor<>(rowMapper)));
    }

    @Nullable
    public <T> T query(String sql, @Nullable Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
        return this.query(sql, this.newArgPreparedStatementSetter(args), rse);
    }

    @Nullable
    public <T> T query(String sql, @Nullable PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
        return this.query((PreparedStatementCreator)(new JdbcTemplate.SimplePreparedStatementCreator(sql)), (PreparedStatementSetter)pss, (ResultSetExtractor<T>)rse);
    }

    @Nullable
    public <T> T query(
            PreparedStatementCreator psc, @Nullable final PreparedStatementSetter pss, final ResultSetExtractor<T> rse)
            throws DataAccessException {

        Assert.notNull(rse, "ResultSetExtractor must not be null");
        logger.debug("Executing prepared SQL query");

        return execute(psc, new PreparedStatementCallback<T>() {
            @Override
            @Nullable
            public T doInPreparedStatement(PreparedStatement ps) throws SQLException {
                ResultSet rs = null;
                try {
                    if (pss != null) {
                        pss.setValues(ps);
                    }
                    rs = ps.executeQuery();
                    return rse.extractData(rs);
                }
                finally {
                    JdbcUtils.closeResultSet(rs);
                    if (pss instanceof ParameterDisposer) {
                        ((ParameterDisposer) pss).cleanupParameters();
                    }
                }
            }
        });
    }

    @Override
    @Nullable
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action)
            throws DataAccessException {

        Assert.notNull(psc, "PreparedStatementCreator must not be null");
        Assert.notNull(action, "Callback object must not be null");
        if (logger.isDebugEnabled()) {
            String sql = getSql(psc);
            logger.debug("Executing prepared SQL statement" + (sql != null ? " [" + sql + "]" : ""));
        }

        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        PreparedStatement ps = null;
        try {
            ps = psc.createPreparedStatement(con);
            applyStatementSettings(ps);
            T result = action.doInPreparedStatement(ps);
            handleWarnings(ps);
            return result;
        }
        catch (SQLException ex) {
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            String sql = getSql(psc);
            psc = null;
            JdbcUtils.closeStatement(ps);
            ps = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw translateException("PreparedStatementCallback", sql, ex);
        }
        finally {
            if (psc instanceof ParameterDisposer) {
                ((ParameterDisposer) psc).cleanupParameters();
            }
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    protected PreparedStatementSetter newArgPreparedStatementSetter(@Nullable Object[] args) {
        return new ArgumentPreparedStatementSetter(args);
    }

    private static <T> T result(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }

    private static class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {
        private final String sql;

        public SimplePreparedStatementCreator(String sql) {
            Assert.notNull(sql, "SQL must not be null");
            this.sql = sql;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            return con.prepareStatement(this.sql);
        }

        @Override
        public String getSql() {
            return this.sql;
        }
    }

    /**
     * Determine SQL from potential provider object.
     * @param sqlProvider object which is potentially an SqlProvider
     * @return the SQL string, or {@code null} if not known
     * @see SqlProvider
     */
    @Nullable
    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        }
        else {
            return null;
        }
    }

    /**
     * Prepare the given JDBC Statement (or PreparedStatement or CallableStatement),
     * applying statement settings such as fetch size, max rows, and query timeout.
     * @param stmt the JDBC Statement to prepare
     * @throws SQLException if thrown by JDBC API
     * see #setFetchSize
     * see #setMaxRows
     * see #setQueryTimeout
     * see org.springframework.jdbc.datasource.DataSourceUtils#applyTransactionTimeout
     */
    protected void applyStatementSettings(Statement stmt) throws SQLException {
        int fetchSize = getFetchSize();
        if (fetchSize != -1) {
            stmt.setFetchSize(fetchSize);
        }
        int maxRows = getMaxRows();
        if (maxRows != -1) {
            stmt.setMaxRows(maxRows);
        }
        DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
    }

    /**
     * Return the fetch size specified for this JdbcTemplate.
     */
    public int getFetchSize() {
        return this.fetchSize;
    }

    /**
     * Return the maximum number of rows specified for this JdbcTemplate.
     */
    public int getMaxRows() {
        return this.maxRows;
    }

    /**
     * Return the query timeout for statements that this JdbcTemplate executes.
     */
    public int getQueryTimeout() {
        return this.queryTimeout;
    }

    /**
     * Throw an SQLWarningException if we're not ignoring warnings,
     * otherwise log the warnings at debug level.
     * @param stmt the current JDBC statement
     * throws SQLWarningException if not ignoring warnings
     * see org.springframework.jdbc.SQLWarningException
     */
    protected void handleWarnings(Statement stmt) throws SQLException {
        if (isIgnoreWarnings()) {
            if (logger.isDebugEnabled()) {
                SQLWarning warningToLog = stmt.getWarnings();
                while (warningToLog != null) {
                    logger.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" +
                            warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
                    warningToLog = warningToLog.getNextWarning();
                }
            }
        }
        else {
            handleWarnings(stmt.getWarnings());
        }
    }

    /**
     * Throw an SQLWarningException if encountering an actual warning.
     * @param warning the warnings object from the current statement.
     * May be {@code null}, in which case this method does nothing.
     * throws SQLWarningException in case of an actual warning to be raised
     */
    protected void handleWarnings(@Nullable SQLWarning warning) throws SQLWarningException {
        if (warning != null) {
            throw new SQLWarningException("Warning not ignored", warning);
        }
    }

    /**
     * Return whether or not we ignore SQLWarnings.
     */
    public boolean isIgnoreWarnings() {
        return this.ignoreWarnings;
    }

    /**
     * Translate the given {@link SQLException} into a generic {@link DataAccessException}.
     * @param task readable text describing the task being attempted
     * @param sql the SQL query or update that caused the problem (may be {@code null})
     * @param ex the offending {@code SQLException}
     * @return a DataAccessException wrapping the {@code SQLException} (never {@code null})
     * @since 5.0
     * @see #getExceptionTranslator()
     */
    protected DataAccessException translateException(String task, @Nullable String sql, SQLException ex) {
        DataAccessException dae = getExceptionTranslator().translate(task, sql, ex);
        return (dae != null ? dae : new UncategorizedSQLException(task, sql, ex));
    }
}
