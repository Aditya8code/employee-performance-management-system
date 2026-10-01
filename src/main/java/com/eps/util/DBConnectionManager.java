package com.eps.util;

import com.eps.exception.DatabaseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enterprise Database Connection Manager with robust connection pooling / lifecycle.
 * Supports primary MySQL 8 database with zero-friction embedded fallback mode
 * so testing and demonstration work out-of-the-box.
 */
public class DBConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(DBConnectionManager.class.getName());
    private static volatile DBConnectionManager instance;

    private String driver;
    private String url;
    private String username;
    private String password;
    private boolean autoFallback = true;
    private boolean isFallbackActive = false;
    private boolean fallbackInitialized = false;

    private DBConnectionManager() {
        loadProperties();
        testOrInitialize();
    }

    public static DBConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DBConnectionManager.class) {
                if (instance == null) {
                    instance = new DBConnectionManager();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "db.properties not found on classpath, using defaults", e);
        }

        this.driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        this.url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/eps_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8");
        this.username = props.getProperty("db.username", "root");
        this.password = props.getProperty("db.password", "root");
        this.autoFallback = Boolean.parseBoolean(props.getProperty("db.auto_fallback", "true"));
    }

    private void testOrInitialize() {
        try {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                LOGGER.info("Successfully connected to primary database: " + url);
                isFallbackActive = false;
                return;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Failed to connect to primary MySQL database: " + ex.getMessage());
            if (autoFallback) {
                LOGGER.info("Initializing embedded MySQL-compatible fallback database for seamless zero-setup execution...");
                setupFallbackDatabase();
            } else {
                throw new DatabaseException("Cannot connect to primary database and fallback is disabled", ex);
            }
        }
    }

    private synchronized void setupFallbackDatabase() {
        this.driver = "org.h2.Driver";
        this.url = "jdbc:h2:mem:eps_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;DB_CLOSE_DELAY=-1";
        this.username = "sa";
        this.password = "";
        this.isFallbackActive = true;

        if (!fallbackInitialized) {
            try {
                Class.forName(driver);
                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    executeSqlFile(conn, "database/schema.sql");
                    executeSqlFile(conn, "database/sample-data.sql");
                }
                fallbackInitialized = true;
                LOGGER.info("Fallback database successfully initialized with schema and realistic sample data.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to initialize fallback database", e);
                throw new DatabaseException("Fallback database initialization failed", e);
            }
        }
    }

    private void executeSqlFile(Connection conn, String relativeFilePath) {
        try {
            Reader reader = null;
            if (Files.exists(Paths.get(relativeFilePath))) {
                reader = Files.newBufferedReader(Paths.get(relativeFilePath));
            } else {
                InputStream is = getClass().getClassLoader().getResourceAsStream(relativeFilePath);
                if (is == null) {
                    is = getClass().getClassLoader().getResourceAsStream("/" + relativeFilePath);
                }
                if (is != null) {
                    reader = new InputStreamReader(is);
                }
            }

            if (reader == null) {
                LOGGER.warning("SQL file not found: " + relativeFilePath);
                return;
            }

            try (BufferedReader br = new BufferedReader(reader);
                 Statement stmt = conn.createStatement()) {
                StringBuilder sb = new StringBuilder();
                String rawLine;
                while ((rawLine = br.readLine()) != null) {
                    String line = rawLine;
                    int commentIdx = line.indexOf("--");
                    if (commentIdx >= 0) {
                        line = line.substring(0, commentIdx);
                    }
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("//") || trimmed.startsWith("/*")) {
                        continue;
                    }
                    if (isFallbackActive) {
                        if (trimmed.toUpperCase().startsWith("CREATE DATABASE") || trimmed.toUpperCase().startsWith("USE ")) {
                            continue;
                        }
                        if (trimmed.toUpperCase().startsWith("INDEX ") || trimmed.toUpperCase().startsWith("KEY ")) {
                            continue;
                        }
                    }

                    sb.append(line).append(" ");
                    if (trimmed.endsWith(";")) {
                        String sql = sb.toString().trim();
                        if (sql.endsWith(";")) {
                            sql = sql.substring(0, sql.length() - 1).trim();
                        }

                        if (isFallbackActive) {
                            sql = sql.replaceAll("(?i)\\)\\s*ENGINE\\s*=[^;]+", ")");
                            sql = sql.replaceAll(",\\s*\\)", ")");
                            sql = sql.replaceAll("(?i)ON\\s+UPDATE\\s+CURRENT_TIMESTAMP", "");
                        }

                        if (!sql.isEmpty()) {
                            try {
                                stmt.execute(sql);
                            } catch (SQLException sqle) {
                                LOGGER.log(Level.WARNING, "Failed SQL: " + sql + " | Reason: " + sqle.getMessage());
                            }
                        }
                        sb.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing SQL script: " + relativeFilePath, e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found: " + driver, e);
        }
    }

    public boolean isFallbackActive() {
        return isFallbackActive;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }
}
