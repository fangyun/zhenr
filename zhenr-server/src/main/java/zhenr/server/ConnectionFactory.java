/**
 * GPL
 */
package zhenr.server;

import java.util.List;

import zhenr.io.Connection;
import zhenr.io.EndPoint;
import zhenr.server.Connector;

/**
 * @author FangYun
 *
 */
public interface ConnectionFactory {
	public String getProtocol();

	public List<String> getProtocols();

	public Connection newConnection(Connector connector, EndPoint endPoint);
}
