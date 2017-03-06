/**
 * GPL
 */
package zhenr;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

import zhenr.logging.Log;
import zhenr.logging.LogFactory;

/**
 * @author FangYun
 *
 */
public class Server {
	private Object lock = new Object();
	private static Log log = LogFactory.getLog(Server.class);
	private final Config config;
	private final int port;

	public static void main(String[] args) {
		try {
			Server srv = new Server();
			srv.start();
			srv.awaitTermination();
		} catch (Exception e) {
			log.error(ZhenrText.get().zhenrStartError, e);
		}
	}

	public Server() {
		config = new Config();
		port = config.getPort();
		init();
	}

	private void init() {
		try {
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			ServerSocket serverSocket = serverChannel.socket();

		} catch (IOException e) {
			throw new ZhenrException(e);
		}
	}

	public void awaitTermination() {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				log.info("Interrupted");
			}
		}
	}

	public void start() {
		run();
		log.info(ZhenrText.get().zhenrRunning);
	}

	private void run() {
		try {
			ServerSocket ss = new ServerSocket(this.port);
			ss.getChannel();
		} catch (IOException e) {
			log.error("Start socket service error.", e);
			throw new ZhenrException(e);
		}
	}

}
