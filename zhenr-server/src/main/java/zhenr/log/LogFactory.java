/**
 * 
 */
package zhenr.util;

/**
 * @author yunfang
 *
 */
public class LogFactory {

	public static Log getLog(Class<?> clazz) {
		return new Log(clazz.getCanonicalName());
	}

}
