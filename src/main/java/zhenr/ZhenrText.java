/**
 * GPL
 */
package zhenr;

import zhenr.nls.NLS;
import zhenr.nls.TranslationBundle;

/**
 * @author FangYun
 *
 */
public class ZhenrText extends TranslationBundle {
	/**
	 * @return an instance of this translation bundle
	 */
	public static ZhenrText get() {
		return NLS.getBundleFor(ZhenrText.class);
	}

	// @formatter:off
	public String zhenrStartError;
	public String zhenrRunning;
}
