package zh.co.common.ibatis;

public class DataSourceSelector {
	private static final ThreadLocal<String> dataSourceNameThreadLocal = new ThreadLocal<String>();

	public static void selectDataSource(String dataSourceName) {
		dataSourceNameThreadLocal.set(dataSourceName);
	}

	public static String getDataSourceName() {
		return dataSourceNameThreadLocal.get();
	}

	public static void selectDefaultDataSource() {
		dataSourceNameThreadLocal.remove();
	}
}
