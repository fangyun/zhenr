/**
 * 
 */
package zhenr.nls;

import java.util.Locale;
import java.util.ResourceBundle;

import zhenr.ZhenrException;

/**
 * @author yunfang
 *
 */
public abstract class TranslationBundleException extends ZhenrException {
	private static final long serialVersionUID = -4359506480499826592L;
	private final Class<?> bundleClass;
	private final Locale locale;

	/**
	 * To construct an instance of {@link TranslationBundleException}
	 *
	 * @param message
	 *            exception message
	 * @param bundleClass
	 *            bundle class for which the exception occurred
	 * @param locale
	 *            locale for which the exception occurred
	 * @param cause
	 *            original exception that caused this exception. Usually thrown
	 *            from the {@link ResourceBundle} class.
	 */
	protected TranslationBundleException(String message, Class<?> bundleClass, Locale locale, Exception cause) {
		super(message, cause);
		this.bundleClass = bundleClass;
		this.locale = locale;
	}

	/**
	 * @return bundle class for which the exception occurred
	 */
	final public Class<?> getBundleClass() {
		return bundleClass;
	}

	/**
	 * @return locale for which the exception occurred
	 */
	final public Locale getLocale() {
		return locale;
	}
}
