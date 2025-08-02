package com.nameless.social.api.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "chatEntityManagerFactory",
		transactionManagerRef = "chatTransactionManager",
		basePackages = {"com.nameless.social.api.repository.chat"}
)
public class ChatDataSourceConfig {
	@Primary
	@Bean
	@ConfigurationProperties("spring.chat-datasource")
	public DataSourceProperties chatDataSourceProperties() {
		DataSourceProperties properties = new DataSourceProperties();
		properties.setDriverClassName("org.mariadb.jdbc.Driver");
		return properties;
	}

	@Primary
	@Bean
	public DataSource chatDataSource(
			@Qualifier("chatDataSourceProperties") DataSourceProperties chatDataSourceProperties
	) {
		return chatDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean chatEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("chatDataSource") DataSource chatDataSource) {
		return builder
				.dataSource(chatDataSource)
				.packages("com.nameless.social.core.entity")
				.persistenceUnit("chat")
				.build();
	}

	@Primary
	@Bean
	public PlatformTransactionManager chatTransactionManager(
			@Qualifier("chatEntityManagerFactory") LocalContainerEntityManagerFactoryBean chatEntityManagerFactory) {
		return new JpaTransactionManager(chatEntityManagerFactory.getObject());
	}

	@Bean
	@ConfigurationProperties(prefix = "chat.liquibase")
	public LiquibaseProperties chatLiquibaseProperties() {
		return new LiquibaseProperties();
	}

	@Bean
	public SpringLiquibase chatLiquibase(
			@Qualifier("chatDataSource") DataSource chatDataSource,
			@Qualifier("chatLiquibaseProperties") LiquibaseProperties liquibaseProperties
	) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(chatDataSource);
		liquibase.setChangeLog(liquibaseProperties.getChangeLog());
		liquibase.setContexts("chat"); // Set the context for chat-related changes
		liquibase.setShouldRun(liquibaseProperties.isEnabled());
		return liquibase;
	}
}
