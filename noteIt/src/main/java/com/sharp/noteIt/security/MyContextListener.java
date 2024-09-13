//package com.sharp.noteIt.security;
//
//import com.zaxxer.hikari.HikariDataSource;
//
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;
//import jakarta.servlet.annotation.WebListener;
//
//@WebListener
//public class MyContextListener implements ServletContextListener {
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        // Explicitly close HikariCP pool here
//        HikariDataSource dataSource = // get your data source
//        if (dataSource != null) {
//            dataSource.close();
//        }
//    }
//}
