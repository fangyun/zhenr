/**
 * 
 */
package zhenr.nls;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author FangYun
 *
 */
public class TranslationBundleLoadingException extends TranslationBundleException {
	private static final long serialVersionUID = 3915205983145632193L;

	/**
	 * Construct a {@link TranslationBundleLoadingException} for the specified
	 * bundle class and locale.
	 *
	 * @param bundleClass
	 *            the bundle class for which the loading failed
	 * @param locale
	 *            the locale for which the loading failed
	 * @param cause
	 *            the original exception thrown from the
	 *            {@link ResourceBundle#getBundle(String, Locale)} method.
	 */
	public TranslationBundleLoadingException(Class<?> bundleClass, Locale locale, Exception cause) {
		super("Loading of translation bundle failed for [" + bundleClass.getName() + ", " + locale.toString() + "]",
				bundleClass, locale, cause);
	}
}
