package stat.config;

/*
 ***************************
 * Copyright (c) 2014      *
 *                         *
 * This code belongs to:   *
 *                         *
 * @author Ahmet Emre Ünal *
 * S001974                 *
 *                         *
 * aemreunal@gmail.com     *
 * emre.unal@ozu.edu.tr    *
 *                         *
 * aemreunal.com           *
 ***************************
 */

import java.beans.PropertyVetoException;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Driver;

@Configuration
@PropertySource(value = GlobalSettings.APP_DB_PROPERTY_SOURCE, ignoreResourceNotFound = true)
public class DatabaseSettings {
    @Value("${" + GlobalSettings.DB_PROPERTY_USERNAME_KEY + ":" + GlobalSettings.DB_DEFAULT_USERNAME + "}")
    private String dbUsername;

    @Value("${" + GlobalSettings.DB_PROPERTY_PASSWORD_KEY + ":" + GlobalSettings.DB_DEFAULT_PASSWORD + "}")
    private String dbPassword;

    @Value("${" + GlobalSettings.DB_PROPERTY_IP_KEY + ":" + GlobalSettings.DB_DEFAULT_IP + "}")
    private String dbIp;

    @Value("${" + GlobalSettings.DB_PROPERTY_PORT_KEY + ":" + GlobalSettings.DB_DEFAULT_PORT + "}")
    private String dbPort;

    @Value("${" + GlobalSettings.DB_PROPERTY_NAME_KEY + ":" + GlobalSettings.DB_DEFAULT_NAME + "}")
    private String dbName;

    @Value("${" + GlobalSettings.DB_PROPERTY_DDL_KEY + ":" + GlobalSettings.HBM2DDL_PROPERTY + "}")
    private String ddlSetting;

    @Value("${" + GlobalSettings.DB_PROPERTY_SHOW_SQL_KEY + ":" + GlobalSettings.SHOW_SQL_PROPERTY + "}")
    private String showSqlQueries;

    @Bean
    public HibernateJpaVendorAdapter vendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        vendorAdapter.setGenerateDdl(true);
        return vendorAdapter;
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(Driver.class.getName());
        } catch (PropertyVetoException e) {
            System.err.println("Unable to set data source driver class!\nWill exit now.");
            System.exit(-1);
        }
        dataSource.setJdbcUrl(getJdbcUrl());
        dataSource.setUser(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put(GlobalSettings.DB_DIALECT_KEY, GlobalSettings.DB_DIALECT_PROPERTY);
        properties.put(GlobalSettings.SHOW_SQL_KEY, showSqlQueries);
        properties.put(GlobalSettings.FORMAT_SQL_KEY, GlobalSettings.FORMAT_SQL_PROPERTY);
        properties.put(GlobalSettings.HBM2DDL_KEY, ddlSetting);
        return properties;
    }

    // We need to call this dynamically and can't make it a field,
    // as the other fields this depends on are assigned dynamically
    // during runtime via '@Value'
    private String getJdbcUrl() {
        return "jdbc:mysql://" + dbIp + ":" + dbPort + "/" + dbName + "?useUnicode=true&characterEncoding=UTF-8";
    }

}
