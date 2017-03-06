/**
 * GPL
 */
package zhenr.nls;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author FangYun
 *
 */
public class TranslationStringMissingException extends TranslationBundleException {
	private static final long serialVersionUID = -4167084345043802402L;
	private final String key;

	/**
	 * Construct a {@link TranslationStringMissingException} for the specified
	 * bundle class, locale and translation key
	 *
	 * @param bundleClass
	 *            the bundle class for which a translation string was missing
	 * @param locale
	 *            the locale for which a translation string was missing
	 * @param key
	 *            the key of the missing translation string
	 * @param cause
	 *            the original exception thrown from the
	 *            {@link ResourceBundle#getString(String)} method.
	 */
	public TranslationStringMissingException(Class<?> bundleClass, Locale locale, String key, Exception cause) {
		super("Translation missing for [" + bundleClass.getName() + ", " + locale.toString() + ", " + key + "]",
				bundleClass, locale, cause);
		this.key = key;
	}

	/**
	 * @return the key of the missing translation string
	 */
	public String getKey() {
		return key;
	}
}
