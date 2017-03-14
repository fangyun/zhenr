/**
 * GPL
 */
package zhenr.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author FangYun
 *
 */
public class Dates {
	private volatile Tick tick;
	private final String formatString;
	private final String tzFormatString;
	private final Locale locale;
	private final SimpleDateFormat tzFormat;

	public static class Tick {
		final long seconds;
		final String string;

		public Tick(long seconds, String string) {
			this.seconds = seconds;
			this.string = string;
		}
	}

	public Dates(String format) {
		this(format, null, TimeZone.getDefault());
	}

	public Dates(String format, Locale l, TimeZone tz) {
		this.formatString = format;
		this.locale = l;

		int zIndex = formatString.indexOf("ZZZ");
		if (zIndex >= 0) {
			String ss1 = formatString.substring(0, zIndex);
			String ss2 = formatString.substring(zIndex + 3);
			int tzOffset = tz.getRawOffset();

			StringBuilder sb = new StringBuilder(formatString.length() + 10);
			sb.append(ss1);
			sb.append("'");
			if (tzOffset >= 0)
				sb.append('+');
			else {
				tzOffset = -tzOffset;
				sb.append('-');
			}

			int raw = tzOffset / (1000 * 60); // Convert to seconds
			int hr = raw / 60;
			int min = raw % 60;

			if (hr < 10)
				sb.append('0');
			sb.append(hr);
			if (min < 10)
				sb.append('0');
			sb.append(min);
			sb.append('\'');

			sb.append(ss2);
			tzFormatString = sb.toString();
		} else
			tzFormatString = formatString;

		if (locale != null) {
			tzFormat = new SimpleDateFormat(tzFormatString, locale);
		} else {
			tzFormat = new SimpleDateFormat(tzFormatString);
		}
		tzFormat.setTimeZone(tz);

		tick = null;
	}

	/**
	 * 秒级的Cache.
	 * 
	 * @param now
	 * @return
	 */
	public String formatNow(long now) {
		long seconds = now / 1000;

		Tick tick = this.tick;

		// Is this the cached time
		if (tick != null && tick.seconds == seconds)
			return tick.string;
		return formatTick(now).string;
	}

	protected Tick formatTick(long now) {
		long seconds = now / 1000;

		// Synchronize to protect _tzFormat
		synchronized (this) {
			// recheck the tick, to save multiple formats
			if (tick == null || tick.seconds != seconds) {
				String s = tzFormat.format(new Date(now));
				return tick = new Tick(seconds, s);
			}
			return tick;
		}
	}
}
