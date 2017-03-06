/**
 * GPL
 */
package zhenr.nls;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The purpose of this class is to provide NLS (National Language Support)
 * configurable per thread.
 *
 * <p>
 * The {@link #setLocale(Locale)} method is used to configure locale for the
 * calling thread. The locale setting is thread inheritable. This means that a
 * child thread will have the same locale setting as its creator thread until it
 * changes it explicitly.
 *
 * <p>
 * Example of usage:
 *
 * <pre>
 * NLS.setLocale(Locale.GERMAN);
 * TransportText t = NLS.getBundleFor(TransportText.class);
 * </pre>
 * 
 * @author FangYun
 */
public class NLS {
	/**
	 * The root locale constant. It is defined here because the Locale.ROOT is
	 * not defined in Java 5
	 */
	public static final Locale ROOT_LOCALE = new Locale("", "", "");

	private static final InheritableThreadLocal<NLS> local = new InheritableThreadLocal<NLS>() {
		protected NLS initialValue() {
			return new NLS(Locale.getDefault());
		}
	};

	/**
	 * Sets the locale for the calling thread.
	 * <p>
	 * The {@link #getBundleFor(Class)} method will honor this setting if if it
	 * is supported by the provided resource bundle property files. Otherwise,
	 * it will use a fall back locale as described in the
	 * {@link TranslationBundle}
	 *
	 * @param locale
	 *            the preferred locale
	 */
	public static void setLocale(Locale locale) {
		local.set(new NLS(locale));
	}

	/**
	 * Sets the JVM default locale as the locale for the calling thread.
	 * <p>
	 * Semantically this is equivalent to
	 * <code>NLS.setLocale(Locale.getDefault())</code>.
	 */
	public static void useJVMDefaultLocale() {
		local.set(new NLS(Locale.getDefault()));
	}

	/**
	 * Returns an instance of the translation bundle of the required type. All
	 * public String fields of the bundle instance will get their values
	 * injected as described in the {@link TranslationBundle}.
	 *
	 * @param <T>
	 *            required bundle type
	 * @param type
	 *            required bundle type
	 * @return an instance of the required bundle type
	 * @exception TranslationBundleLoadingException
	 *                see {@link TranslationBundleLoadingException}
	 * @exception TranslationStringMissingException
	 *                see {@link TranslationStringMissingException}
	 */
	public static <T extends TranslationBundle> T getBundleFor(Class<T> type) {
		return local.get().get(type);
	}

	final private Locale locale;
	@SuppressWarnings("rawtypes")
	final private ConcurrentHashMap<Class, TranslationBundle> map = new ConcurrentHashMap<Class, TranslationBundle>();

	private NLS(Locale locale) {
		this.locale = locale;
	}

	@SuppressWarnings("unchecked")
	private <T extends TranslationBundle> T get(Class<T> type) {
		TranslationBundle bundle = map.get(type);
		if (bundle == null) {
			bundle = GlobalBundleCache.lookupBundle(locale, type);
			// There is a small opportunity for a race, which we may
			// lose. Accept defeat and return the winner's instance.
			TranslationBundle old = map.putIfAbsent(type, bundle);
			if (old != null)
				bundle = old;
		}
		return (T) bundle;
	}
}