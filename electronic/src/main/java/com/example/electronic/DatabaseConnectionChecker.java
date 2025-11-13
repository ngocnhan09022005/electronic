package com.example.electronic;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Safe startup check that attempts to get a JDBC connection if a DataSource is present.
 * - If no DataSource bean exists, it logs that no datasource is configured.
 * - If a DataSource exists, it attempts to open and close a connection and logs metadata.
 */
@Component
public class DatabaseConnectionChecker implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionChecker.class);

    @Autowired(required = false)
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        if (dataSource == null) {
            log.info("DatabaseConnectionChecker: No DataSource bean found — application is not configured to use a database.");
            return;
        }

        log.info("DatabaseConnectionChecker: DataSource bean detected, attempting to get a connection...");
        try (Connection conn = dataSource.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                DatabaseMetaData meta = conn.getMetaData();
                log.info("Database connection successful: URL={} Product={} {}, AutoCommit={}",
                        meta.getURL(), meta.getDatabaseProductName(), meta.getDatabaseProductVersion(), conn.getAutoCommit());
            } else {
                log.warn("DatabaseConnectionChecker: Obtained a connection but it appears closed or null.");
            }
        } catch (Exception ex) {
            log.error("DatabaseConnectionChecker: Failed to obtain a connection from DataSource", ex);
        }
    }
}
