package org.ws.tanyunshou.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author yinan
 * @date created in 下午4:36 18-12-25
 */
@Configuration
public class TransactionConfig {

    private final static String DEFAULT = "master_tr";
    private final static String SLAVE = "slave_tr";

    @Bean(name = TransactionConfig.DEFAULT)
    public DataSourceTransactionManager transactionManager(@Qualifier(DataSourceNames.MASTER)DataSource masterDataSource) {
        return new DataSourceTransactionManager(masterDataSource);
    }

    @Bean(name = TransactionConfig.SLAVE)
    public DataSourceTransactionManager slaveTransactionManager(@Qualifier(DataSourceNames.SLAVE) DataSource slaveDataSource) {
        return new DataSourceTransactionManager(slaveDataSource);
    }


}
