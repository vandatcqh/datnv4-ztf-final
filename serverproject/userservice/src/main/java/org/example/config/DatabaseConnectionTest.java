package org.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTest.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && connection.isValid(2)) {
                logger.info("Database connection successful!");
            } else {
                logger.error("Database connection failed!");
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage());
        }
    }
}