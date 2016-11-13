package zh.co.common.utils;

import java.sql.Connection;

import javax.sql.DataSource;




import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * <p>[概要] TransactionUtils</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class TransactionUtils {

	public static PlatformTransactionManager getTransactionManager() {
		return (PlatformTransactionManager) SpringAppContextManager
				.getBean("transactionManager");
	}

	public static DataSource getDataSource() {
		return (DataSource) SpringAppContextManager.getBean("dataSource");
	}

	public static Connection getConnection(DataSource dataSource) {
		return DataSourceUtils.getConnection(dataSource);
	}

	public static Connection getConnection() {
		return getConnection(getDataSource());
	}

	public static void releaseConnection(DataSource dataSource,
			Connection connection) {
		DataSourceUtils.releaseConnection(connection, dataSource);
	}

	public static void releaseConnection(Connection connection) {
		releaseConnection(getDataSource(), connection);
	}
}
