package org.bobpark.bobsonserverapi.configure.flyway;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.flywaydb.core.Flyway;

@Configuration
public class FlywayConfiguration {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {

        return Flyway.configure()
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .dataSource(dataSource)
                .table("bobson_migration_history")
                .locations("classpath:db/migration")
                .load();
    }
}
