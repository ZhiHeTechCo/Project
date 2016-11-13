package zh.co.common.ibatis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import zh.co.item.bank.model.entity.OptionBean;

/**
 * {@link org.springframework.beans.factory.FactoryBean} that creates an iBATIS3
 * {@link org.apache.ibatis.session.SqlSessionFactory}. This is the usual way to
 * set up a shared iBATIS3 SqlSessionFactory in a Spring application context;
 * the SqlSessionFactory can then be passed to iBATIS-based DAOs via dependency
 * injection.
 * 
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>,
        InitializingBean {

    private SqlSessionFactory sqlSessionFactory;

    private DataSource dataSource;

    private TransactionFactory transactionFactory;

    private Resource[] mapperLocations;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public SqlSessionFactory getObject() throws Exception {
        return this.sqlSessionFactory;
    }

    public Class<?> getObjectType() {
        return ((this.sqlSessionFactory != null) ? this.sqlSessionFactory
                .getClass() : SqlSessionFactory.class);
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if (transactionFactory == null) {
            transactionFactory = new ManagedTransactionFactory();
        }
        Environment environment = new Environment("default",
                transactionFactory, dataSource);
        Configuration config = new DynaConfiguration(environment);
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = factoryBuilder.build(config);

        // register type alias
        config.getTypeAliasRegistry().registerAlias("option",
                OptionBean.class.getName());

        // collect SQL mapper files
        List<File> sqlFiles = new ArrayList<File>();
        // mappers registered in application context
        if (mapperLocations != null) {
            for (Resource mapperLocation : mapperLocations) {
                sqlFiles.add(mapperLocation.getFile());
            }
        }
        // mappers of inventory
        sqlFiles.addAll(XmlMapperUtils.listDynamicSqlFiles());

        // load each SQL mapper files
        for (File sqlFile : sqlFiles) {
        	XmlMapperUtils.addMapper(config, sqlFile);
        	XmlMapperUtils.addCountSql(config, sqlFile);
        	SqlMapperManager.addSqlMapperInfo(sqlFile.getPath(), sqlFile
                    .lastModified());
        }
    }

}
