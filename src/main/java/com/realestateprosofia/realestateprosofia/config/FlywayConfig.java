package com.realestateprosofia.realestateprosofia.config;

import com.realestateprosofia.realestateprosofia.utils.CustomDataSourceBuilder;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

    private final CustomDataSourceBuilder customDataSourceBuilder;
    private final FlywayProperties flywayProperties;

    public Flyway configureFlyway(final String tenant) {
        final DataSource tenantDataSource = customDataSourceBuilder.buildDataSource(tenant);

        return Flyway.configure()
                .dataSource(tenantDataSource)
                .locations(flywayProperties.getLocations().toArray(new String[0]))
                .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
                .schemas(tenant)
                .load();
    }

    public void migrateForTenant(final String tenant) {
        configureFlyway(tenant).migrate();
    }

    @Bean
    public ApplicationRunner flywayMigrationRunner() {
        return args -> {
            migrateForTenant("tenant");
        };
    }
}