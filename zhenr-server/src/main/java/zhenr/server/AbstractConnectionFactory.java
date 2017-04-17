/**
 * GPL
 */
package zhenr.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author FangYun
 *
 */
public abstract class AbstractConnectionFactory implements ConnectionFactory {
	private final String _protocol;
	private final List<String> _protocols;

	protected AbstractConnectionFactory(String protocol) {
		_protocol = protocol;
		_protocols = Collections.unmodifiableList(Arrays.asList(new String[] { protocol }));
	}

	protected AbstractConnectionFactory(String... protocols) {
		_protocol = protocols[0];
		_protocols = Collections.unmodifiableList(Arrays.asList(protocols));
	}

	@Override
	public String getProtocol() {
		return _protocol;
	}

	@Override
	public List<String> getProtocols() {
		return _protocols;
	}

	@Override
	public String toString() {
		return String.format("%s@%x%s", this.getClass().getSimpleName(), hashCode(), getProtocols());
	}
}
