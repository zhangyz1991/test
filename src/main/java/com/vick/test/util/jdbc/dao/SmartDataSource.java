package com.vick.test.util.jdbc.dao;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface SmartDataSource extends DataSource {

    /**
     * Should we close this Connection, obtained from this DataSource?
     * <p>Code that uses Connections from a SmartDataSource should always
     * perform a check via this method before invoking {@code close()}.
     * <p>Note that the JdbcTemplate class in the 'jdbc.core' package takes care of
     * releasing JDBC Connections, freeing application code of this responsibility.
     * @param con the Connection to check
     * @return whether the given Connection should be closed
     * @see java.sql.Connection#close()
     */
    boolean shouldClose(Connection con);

}
