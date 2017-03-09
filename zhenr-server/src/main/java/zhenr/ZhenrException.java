/**
 * GPL
 */
package zhenr;

/**
 * @author FangYun
 *
 */
public class ZhenrException extends RuntimeException {
	private static final long serialVersionUID = 7340795847184650391L;

	public ZhenrException() {
		super();
	}

	public ZhenrException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ZhenrException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZhenrException(String message) {
		super(message);
	}

	public ZhenrException(Throwable cause) {
		super(cause);
	}
}
