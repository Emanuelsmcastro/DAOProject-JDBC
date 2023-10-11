package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DB {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return conn;
    }

    public static Connection getConnection(boolean autoCommit) {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        try {
            conn.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        if (st == null)
            return;
        try {
            st.close();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs == null)
            return;
        try {
            rs.close();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

}
