/**
 * 
 */
package zhenr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import zhenr.log.Log;
import zhenr.log.LogFactory;

/**
 * @author yunfang
 *
 */
public class Config {
	private Log log = LogFactory.getLog(Config.class);
	private String zhenrHome;
	private int port;

	public Config() {
		try {
			zhenrHome = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getCanonicalPath();
			System.setProperty("zhenr.home", zhenrHome);
			File configFile = new File(zhenrHome, "conf/zhenr.xml");
			InputStream stream = null;
			if (!configFile.exists()) {
				log.info("Not exists conf/zhenr.xml, use default configuration file.");
				stream = Config.class.getResourceAsStream("/META-INF/conf/zhenr.default.xml");
			} else {
				stream = new FileInputStream(configFile);
			}
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
			XPath xPath = XPathFactory.newInstance().newXPath();
			port = Integer.parseInt(xPath.evaluate("/zhenr/server/@port", doc));
		} catch (IOException | URISyntaxException | SAXException | ParserConfigurationException
				| XPathExpressionException e) {
			throw new ZhenrException("Config init error!");
		}
	}

	public String getZhenrHome() {
		return zhenrHome;
	}

	public int getPort() {
		return port;
	}
}
