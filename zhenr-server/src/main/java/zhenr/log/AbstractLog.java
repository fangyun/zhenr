/**
 * GPL
 */
package zhenr.log;

import java.util.Properties;

import zhenr.util.Strings;

/**
 * @author FangYun
 *
 */
public abstract class AbstractLog implements Log {
	public static final int LEVEL_DEFAULT = -1;
	public static final int LEVEL_ALL = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_WARN = 3;
	public static final int LEVEL_OFF = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see zhenr.log.Log#getLog(java.lang.String)
	 */
	@Override
	public Log getLog(String name) {
		if (Strings.isBlank(name)) {
			return this;
		}
		final String basename = getName();
		final String fullname = (Strings.isBlank(basename) || LogFactory.getRootLog() == this) ? name
				: (basename + "." + name);
		Log log = LogFactory.getLogs().get(fullname);
		if (log == null) {
			Log newlog = newLog(fullname);

			log = LogFactory.getMutableLogs().putIfAbsent(fullname, newlog);
			if (log == null) {
				log = newlog;
			}
		}

		return log;
	}

	protected abstract Log newLog(String fullname);

	protected static String condensePackageString(String classname) {
		String[] parts = classname.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.length - 1; i++) {
			sb.append(parts[i].charAt(0));
		}
		if (sb.length() > 0) {
			sb.append(".");
		}
		sb.append(parts[parts.length - 1]);
		return sb.toString();
	}

	/**
	 * 从属性集中匹配最大长度段的LEVEL的值.
	 * 
	 * @param props
	 * @param name
	 * @return
	 */
	public static int lookupLogLevel(Properties props, final String name) {
		if ((props == null) || (props.isEmpty()) || name == null) {
			return LEVEL_DEFAULT;
		}

		String nameSegment = name;

		while ((nameSegment != null) && (nameSegment.length() > 0)) {
			String levelStr = props.getProperty(nameSegment + ".LEVEL");
			int level = getLevelId(nameSegment + ".LEVEL", levelStr);
			if (level != (-1)) {
				return level;
			}

			// Trim and try again.
			int idx = nameSegment.lastIndexOf('.');
			if (idx >= 0) {
				nameSegment = nameSegment.substring(0, idx);
			} else {
				nameSegment = null;
			}
		}
		// Default Logging Level
		return LEVEL_DEFAULT;
	}

	protected static int getLevelId(String levelSegment, String levelName) {
		if (levelName == null) {
			return -1;
		}
		String levelStr = levelName.trim();
		if ("ALL".equalsIgnoreCase(levelStr)) {
			return LEVEL_ALL;
		} else if ("DEBUG".equalsIgnoreCase(levelStr)) {
			return LEVEL_DEBUG;
		} else if ("INFO".equalsIgnoreCase(levelStr)) {
			return LEVEL_INFO;
		} else if ("WARN".equalsIgnoreCase(levelStr)) {
			return LEVEL_WARN;
		} else if ("OFF".equalsIgnoreCase(levelStr)) {
			return LEVEL_OFF;
		}

		System.err.println("Unknown StdErrLog level [" + levelSegment + "]=[" + levelStr
				+ "], expecting only [ALL, DEBUG, INFO, WARN, OFF] as values.");
		return -1;
	}

	/**
	 * 从属性集中匹配最大长度段的nameSegment.property的值.
	 * 
	 * @param props
	 * @param name
	 * @param property
	 * @return
	 */
	public static String getLogProperty(Properties props, String name, String property) {
		// Calculate the level this named logger should operate under.
		String nameSegment = name;

		while ((nameSegment != null) && (nameSegment.length() > 0)) {
			String s = props.getProperty(nameSegment + "." + property);
			if (s != null)
				return s;

			// Trim and try again.
			int idx = nameSegment.lastIndexOf('.');
			nameSegment = (idx >= 0) ? nameSegment.substring(0, idx) : null;
		}

		return null;
	}
}
