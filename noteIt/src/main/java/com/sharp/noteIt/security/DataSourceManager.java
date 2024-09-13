package com.sharp.noteIt.security;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceManager {
    
    private HikariDataSource dataSource;

    public void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/yourdb");
        config.setUsername("root");
        config.setPassword("yourpassword");
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    public void shutdown() {
        if (dataSource != null) {
            System.out.println("Shutting down HikariDataSource...");
            dataSource.close();
        }
    }
}

