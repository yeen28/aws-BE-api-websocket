package com.nameless.social.api.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "userEntityManagerFactory",
		transactionManagerRef = "userTransactionManager",
		basePackages = {"com.nameless.social.api.repository.user"}
)
public class UserDataSourceConfig {
	@Bean
	@ConfigurationProperties("spring.user-datasource")
	public DataSourceProperties userDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource userDataSource(@Qualifier("userDataSourceProperties") DataSourceProperties userDataSourceProperties) {
		return userDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("userDataSource") DataSource userDataSource) {
		return builder
				.dataSource(userDataSource)
				.packages("com.nameless.social.core.entity")
				.persistenceUnit("user")
				.build();
	}

	@Bean
	public PlatformTransactionManager userTransactionManager(
			@Qualifier("userEntityManagerFactory") LocalContainerEntityManagerFactoryBean userEntityManagerFactory) {
		return new JpaTransactionManager(userEntityManagerFactory.getObject());
	}

	@Bean
	@ConfigurationProperties(prefix = "user.liquibase")
	public LiquibaseProperties userLiquibaseProperties() {
		return new LiquibaseProperties();
	}

	@Bean
	public SpringLiquibase userLiquibase(
			@Qualifier("userDataSource") DataSource userDataSource,
			@Qualifier("userLiquibaseProperties") LiquibaseProperties liquibaseProperties
	) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(userDataSource);
		liquibase.setChangeLog(liquibaseProperties.getChangeLog());
		liquibase.setContexts("user"); // Set the context for user-related changes
		liquibase.setShouldRun(liquibaseProperties.isEnabled());
		return liquibase;
	}
}
