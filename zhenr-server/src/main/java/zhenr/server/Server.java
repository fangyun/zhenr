/**
 * GPL
 */
package zhenr.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import zhenr.util.thread.QueuedThreadPool;

import zhenr.log.Log;
import zhenr.log.LogFactory;
import zhenr.util.Attributes;
import zhenr.util.AttributesMap;
import zhenr.util.thread.ThreadPool;

/**
 * Notes: Remove all beans operations.
 * 
 * @author FangYun
 *
 */
public class Server implements Attributes {
	private static final Log LOG = LogFactory.getLog(Server.class);
	private final AttributesMap _attributes = new AttributesMap();
	private final ThreadPool _threadPool;
	private final List<Connector> _connectors = new CopyOnWriteArrayList<>();

	public Server() {
		this((ThreadPool) null);
	}

	public Server(ThreadPool pool) {
		_threadPool = pool != null ? pool : new QueuedThreadPool();
		setServer(this);
	}

	public Connector[] getConnectors() {
		List<Connector> connectors = new ArrayList<>(_connectors);
		return connectors.toArray(new Connector[connectors.size()]);
	}

	public void addConnector(Connector connector) {
		if (connector.getServer() != this)
			throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server "
					+ connector.getServer() + " and server " + this);
		_connectors.add(connector);
	}

	public void removeConnector(Connector connector) {
		_connectors.remove(connector);
	}

	public void setConnectors(Connector[] connectors) {
		if (connectors != null) {
			for (Connector connector : connectors) {
				if (connector.getServer() != this)
					throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server "
							+ connector.getServer() + " and server " + this);
			}
		}

		Connector[] oldConnectors = getConnectors();
		_connectors.removeAll(Arrays.asList(oldConnectors));
		if (connectors != null)
			_connectors.addAll(Arrays.asList(connectors));
	}

	@Override
	public void removeAttribute(String name) {
		_attributes.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object attribute) {
		_attributes.setAttribute(name, attribute);
	}

	@Override
	public Object getAttribute(String name) {
		return _attributes.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return AttributesMap.getAttributeNamesCopy(_attributes);
	}

	@Override
	public void clearAttributes() {
		_attributes.clearAttributes();
	}
}
