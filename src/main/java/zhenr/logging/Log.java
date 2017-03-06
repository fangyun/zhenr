/**
 * 
 */
package zhenr.logging;

/**
 * @author yunfang
 *
 */
public class Log {
	private String name;

	public Log(String name) {
		this.name = name;
	}

	public void info(String msg) {
		System.out.println(String.format("[INFO] %s: %s", name, msg));
	}

	public void error(String msg, Throwable e) {
		System.out.println(String.format("[ERROR] %s: %s", name, msg));
		e.printStackTrace();
	}

}
