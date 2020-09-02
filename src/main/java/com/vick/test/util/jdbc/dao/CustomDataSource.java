package com.vick.test.util.jdbc.dao;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class CustomDataSource implements DataSource {

    private static LinkedList<Connection> pool = new LinkedList<Connection>();

    /*private static final String name = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://139.196.165.243:3306/vick_test";
    private static final String user = "root";
    private static final String password = "success";

    static{//利用静态代码块儿在类一加载的时候就初始化10个连接到池中
        try {
            Class.forName(name);
            for(int i=0;i<10;i++){
                Connection conn = DriverManager.getConnection(url, user, password);
                pool.add(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private static String name;
    private static String url;
    private static String user;
    private static String password;

    public CustomDataSource(String name, String url, String user, String password) {
        CustomDataSource.name = name;
        CustomDataSource.url = url;
        CustomDataSource.user = user;
        CustomDataSource.password = password;
        init();
    }

    private void init() {
        try {
            Class.forName(name);
            for (int i = 0; i < 1; i++) {
                Connection conn = DriverManager.getConnection(url, user, password);
                pool.add(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (pool.size() > 0) {
            final Connection conn = pool.remove();
            //返回动态代理对象
            return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable {
                    if ("close".equals(method.getName())) {
                        return pool.add(conn);
                    } else {
                        return method.invoke(conn, args);
                    }
                }
            });
        } else {
            throw new RuntimeException("对不起，服务器忙...");
        }

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
