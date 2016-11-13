package zh.co.common.ibatis;

public class IbatisContext {
	private static class Config {
		private boolean callSettersOnNulls = false;

		public boolean isCallSettersOnNulls() {
			return callSettersOnNulls;
		}

		public void setCallSettersOnNulls(boolean callSettersOnNulls) {
			this.callSettersOnNulls = callSettersOnNulls;
		}

	}

	private static final ThreadLocal<Config> configThreadLocal = new ThreadLocal<Config>();

	/**
	 * This method never returns null. A new instance of <code>Config</code> is
	 * created when necessary.
	 */
	private static Config getConfig() {
		Config config = configThreadLocal.get();
		if (config == null) {
			config = new Config();
			configThreadLocal.set(config);
		}
		return config;
	}

	public static void setCallSettersOnNulls(boolean callSettersOnNulls) {
		getConfig().setCallSettersOnNulls(callSettersOnNulls);
	}

	public static boolean isCallSettersOnNulls() {
		return getConfig().isCallSettersOnNulls();
	}

}
