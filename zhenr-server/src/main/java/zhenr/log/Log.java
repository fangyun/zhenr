package zhenr.log;

public interface Log {
	public String getName();

	public void warn(String msg, Object... args);

	public void warn(String msg, Throwable thrown);

	public void info(String msg, Object... args);

	public void info(String msg, Throwable thrown);

	public boolean isDebugEnabled();

	public void debug(String msg, Object... args);

	public void debug(String msg, Throwable thrown);

	public Log getLog(String name);
}
