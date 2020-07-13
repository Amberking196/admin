package com.server.dbpool;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class TransactionConfiguration {
	
	
	@Bean
	public TransactionAwareDataSourceProxy createProxy(){
		BasicDataSource datasource = DBPool.getInstance().getDatasource();
		TransactionAwareDataSourceProxy transactionProxy = new TransactionAwareDataSourceProxy(datasource);
		return transactionProxy;
	}

	
	@Bean
	public DataSourceTransactionManager createTraManager(){
		BasicDataSource datasource = DBPool.getInstance().getDatasource();
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(datasource);
		return dataSourceTransactionManager;
	}
}
