package com.thegoate.spring.boot;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

/**
 * Created by Eric Angeli on 11/1/2017.
 */
@Configuration
public class SimpleOracleDataSource {
    private String username="c##simpletest";

    private String password="simple";

    private String url="jdbc:oracle:thin:@dbcontainer.local.onedatascan.io:1521:DL124X01";

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Autowired
    Environment env;

    @Bean
    OracleDataSource dataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setURL(env.getProperty("spring.datasource.url"));
 //       dataSource.setImplicitCachingEnabled(true);
 //       dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager() throws SQLException {
//        OracleLocalTransaction
        return new DataSourceTransactionManager(dataSource());
    }
}
