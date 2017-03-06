/**
 * GPL
 */
package zhenr.nls;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author FangYun
 *
 */
public abstract class TranslationBundle {
	private Locale effectiveLocale;
	private ResourceBundle resourceBundle;

	/**
	 * @return the effective locale used for loading the resource bundle from
	 *         which the field values were taken
	 */
	public Locale effectiveLocale() {
		return effectiveLocale;
	}

	/**
	 * @return the resource bundle on which this translation bundle is based
	 */
	public ResourceBundle resourceBundle() {
		return resourceBundle;
	}

	/**
	 * Injects locale specific text in all instance fields of this instance.
	 * Only public instance fields of type <code>String</code> are considered.
	 * <p>
	 * The name of this (sub)class plus the given <code>locale</code> parameter
	 * define the resource bundle to be loaded. In other words the
	 * <code>this.getClass().getName()</code> is used as the
	 * <code>baseName</code> parameter in the
	 * {@link ResourceBundle#getBundle(String, Locale)} parameter to load the
	 * resource bundle.
	 * <p>
	 *
	 * @param locale
	 *            defines the locale to be used when loading the resource bundle
	 * @exception TranslationBundleLoadingException
	 *                see {@link TranslationBundleLoadingException}
	 * @exception TranslationStringMissingException
	 *                see {@link TranslationStringMissingException}
	 */
	void load(Locale locale) throws TranslationBundleLoadingException {
		Class<? extends TranslationBundle> bundleClass = getClass();
		try {
			resourceBundle = ResourceBundle.getBundle(bundleClass.getName(), locale, bundleClass.getClassLoader());
		} catch (MissingResourceException e) {
			throw new TranslationBundleLoadingException(bundleClass, locale, e);
		}
		this.effectiveLocale = resourceBundle.getLocale();

		for (Field field : bundleClass.getFields()) {
			if (field.getType().equals(String.class)) {
				try {
					String translatedText = resourceBundle.getString(field.getName());
					field.set(this, translatedText);
				} catch (MissingResourceException e) {
					throw new TranslationStringMissingException(bundleClass, locale, field.getName(), e);
				} catch (IllegalArgumentException e) {
					throw new Error(e);
				} catch (IllegalAccessException e) {
					throw new Error(e);
				}
			}
		}
	}
}
