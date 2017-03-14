/**
 * 
 */
package zhenr.log;

import java.io.PrintStream;
import java.util.Properties;

import zhenr.util.Dates;

/**
 * @author FangYun
 *
 */
public class StdErrLog extends AbstractLog {
	private static final String EOL = System.getProperty("line.separator");
	private static Dates dateCache;
	private final String name;
	private final String abbrevname;
	private int level;
	private PrintStream stderr = null;

	static {
		try {
			dateCache = new Dates("yyyy-MM-dd HH:mm:ss");
		} catch (Exception x) {
			x.printStackTrace(System.err);
		}
	}

	public StdErrLog() {
		this(null);
	}

	public StdErrLog(String name) {
		this(name, null);
	}

	public StdErrLog(String name, Properties props) {
		if (props != null && props != LogFactory.PROPERTIES) {
			LogFactory.PROPERTIES.putAll(props);
		}
		this.name = name == null ? "" : name;
		this.abbrevname = condensePackageString(this.name);
		this.level = getLogLevel(LogFactory.PROPERTIES, this.name);
		this.stderr = System.err;
	}

	public void setStdErrStream(PrintStream stream) {
		if (stream != null) {
			this.stderr = stream;
		}
	}

	public static int getLogLevel(Properties props, String name) {
		int level = lookupLogLevel(props, name);
		if (level == LEVEL_DEFAULT) {
			level = lookupLogLevel(props, "log");
			if (level == LEVEL_DEFAULT) {
				level = LEVEL_INFO;
			}
		}
		return level;
	}

	public void warn(String msg, Object... args) {
		if (level <= LEVEL_WARN) {
			StringBuilder buffer = new StringBuilder(64);
			format(buffer, ":WARN:", msg, args);
			stderr.println(buffer);
		}
	}

	public void warn(String msg, Throwable thrown) {
		if (level <= LEVEL_WARN) {
			StringBuilder buffer = new StringBuilder(64);
			format(buffer, ":WARN:", msg, thrown);
			stderr.println(buffer);
		}
	}

	public void info(String msg, Object... args) {
		if (level <= LEVEL_INFO) {
			StringBuilder buffer = new StringBuilder(64);
			format(buffer, ":INFO:", msg, args);
			stderr.println(buffer);
		}
	}

	public void info(String msg, Throwable thrown) {
		if (level <= LEVEL_INFO) {
			StringBuilder buffer = new StringBuilder(64);
			format(buffer, ":INFO:", msg, thrown);
			stderr.println(buffer);
		}
	}

	public boolean isDebugEnabled() {
		return (level <= LEVEL_DEBUG);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void debug(String msg, Object... args) {
		if (level <= LEVEL_DEBUG) {
			StringBuilder buffer = new StringBuilder(64);
			format(buffer, ":DBUG:", msg, args);
			(stderr == null ? System.err : stderr).println(buffer);
		}
	}

	public void debug(String msg, Throwable thrown) {
		if (level <= LEVEL_DEBUG) {
			StringBuilder buffer = new StringBuilder(64);
			format(buffer, ":DBUG:", msg, thrown);
			(stderr == null ? System.err : stderr).println(buffer);
		}
	}

	/**
	 * Create a Child Logger of this Logger.
	 */
	@Override
	protected Log newLog(String fullname) {
		StdErrLog logger = new StdErrLog(fullname);
		logger.stderr = this.stderr;
		return logger;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("StdErrLog:");
		s.append(name);
		s.append(":LEVEL=");
		switch (level) {
		case LEVEL_ALL:
			s.append("ALL");
			break;
		case LEVEL_DEBUG:
			s.append("DEBUG");
			break;
		case LEVEL_INFO:
			s.append("INFO");
			break;
		case LEVEL_WARN:
			s.append("WARN");
			break;
		default:
			s.append("?");
			break;
		}
		return s.toString();
	}

	private void format(StringBuilder buffer, String level, String msg, Throwable thrown) {
		format(buffer, level, msg);
		format(buffer, thrown);
	}

	protected void format(StringBuilder buffer, Throwable thrown) {
		format(buffer, thrown, "");
	}

	protected void format(StringBuilder buffer, Throwable thrown, String indent) {
		if (thrown == null) {
			buffer.append("null");
		} else {
			buffer.append(EOL).append(indent);
			format(buffer, thrown.toString());
			StackTraceElement[] elements = thrown.getStackTrace();
			for (int i = 0; elements != null && i < elements.length; i++) {
				buffer.append(EOL).append(indent).append("\tat ");
				format(buffer, elements[i].toString());
			}

			for (Throwable suppressed : thrown.getSuppressed()) {
				buffer.append(EOL).append(indent).append("Suppressed: ");
				format(buffer, suppressed, "\t|" + indent);
			}

			Throwable cause = thrown.getCause();
			if (cause != null && cause != thrown) {
				buffer.append(EOL).append(indent).append("Caused by: ");
				format(buffer, cause, indent);
			}
		}
	}

	private void format(StringBuilder buffer, String level, String msg, Object... args) {
		long now = System.currentTimeMillis();
		int ms = (int) (now % 1000);
		String date = dateCache.formatNow(now);
		tag(buffer, date, ms, level);
		format(buffer, msg, args);
	}

	private void tag(StringBuilder buffer, String date, int ms, String tag) {
		buffer.setLength(0);
		buffer.append(date);
		if (ms > 99) {
			buffer.append('.');
		} else if (ms > 9) {
			buffer.append(".0");
		} else {
			buffer.append(".00");
		}
		buffer.append(ms).append(tag);

		String name = abbrevname;
		String tname = Thread.currentThread().getName();
		buffer.append(name).append(':').append(tname).append(':');
		buffer.append(' ');
	}

	private void format(StringBuilder builder, String msg, Object... args) {
		if (msg == null) {
			msg = "";
			for (int i = 0; i < args.length; i++) {
				msg += "{} ";
			}
		}
		String braces = "{}";
		int start = 0;
		for (Object arg : args) {
			int bracesIndex = msg.indexOf(braces, start);
			if (bracesIndex < 0) {
				builder.append(msg.substring(start));
				builder.append(" ");
				builder.append(arg);
				start = msg.length();
			} else {
				builder.append(msg.substring(start, bracesIndex));
				builder.append(String.valueOf(arg));
				start = bracesIndex + braces.length();
			}
		}
		builder.append(msg.substring(start));
	}

	public void error(String msg, Throwable e) {
		System.err.printf("[ERROR] %s: %s", name, msg);
		e.printStackTrace();
	}

	@Override
	public String getName() {
		return this.name;
	}
}
