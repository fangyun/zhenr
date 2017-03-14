/**
 * GPL
 */
package zhenr.log;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import zhenr.util.Strings;

/**
 * @author FangYun
 *
 */
public class LogFactory {
	protected final static Properties PROPERTIES = new Properties();
	private static Log LOG;
	private static boolean initialized = false;
	private final static ConcurrentMap<String, Log> LOGS = new ConcurrentHashMap<>();

	static {
		loadProperties();
	}

	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getCanonicalName());
	}

	public static Log getLog(String name) {
		initialized();
		if (Strings.isBlank(name)) {
			return LOG;
		}
		Log log = LOGS.get(name);
		if (log == null) {
			log = LOG.getLog(name);
		}
		return log;
	}

	/**
	 * 初始化加载zhenr-log.properties和系统属性.
	 */
	private static void loadProperties() {
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			@Override
			public Void run() {
				try {
					PROPERTIES.load(LogFactory.class.getResourceAsStream("/META-INF/conf/zhenr-log.properties"));
					Properties p = System.getProperties();
					for (Object k : p.keySet()) {
						String key = (String) k;
						String value = (String) p.getProperty(key);
						if (value != null) {
							PROPERTIES.setProperty(key, value);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	public static Log getRootLog() {
		initialized();
		return LOG;
	}

	private static void initialized() {
		synchronized (LogFactory.class) {
			if (initialized) {
				return;
			}
			initialized = true;
			String clazz = PROPERTIES.getProperty("zhenr.log.class");
			if (clazz == null) {
				LOG = new StdErrLog();
			} else {
				try {
					LOG = (Log) (LogFactory.class.getClassLoader().loadClass(clazz).newInstance());
				} catch (Throwable e) {
					LOG = new StdErrLog();
				}
			}
		}
	}

	public static Map<String, Log> getLogs() {
		return Collections.unmodifiableMap(LOGS);
	}

	static ConcurrentMap<String, Log> getMutableLogs() {
		return LOGS;
	}

}
