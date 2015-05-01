package com.missionarogya.logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class InsertBMJData {

	public static void xmain() throws ParserConfigurationException,
			MalformedURLException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db
				.parse(new URL(
						"http://open-archive.highwire.org/handler?verb=ListRecords&metadataPrefix=oai_dc&set=casereports")
						.openStream());
		NodeList nodelist = doc.getElementsByTagName("*");
		String s = null;
		for (int i = 0; i < nodelist.getLength(); ++i) {
			Node node = nodelist.item(i);
			String name = node.getNodeName();
			if (name.equals("dc:title")) {
				s = name
						+ "#"
						+ nodelist.item(i).getChildNodes().item(0)
								.getNodeValue() + "\n";
			}
			if (name.equals("dc:description")) {
				s += name
						+ "#"
						+ nodelist.item(i).getChildNodes().item(0)
								.getNodeValue() + "\n";
				// Adding the string to the database
				DBHandler.insertXMLString(s);
			}
			/*
			 * System.out.print(node.getNodeName() + " : "); NodeList sublist =
			 * nodelist.item(i).getChildNodes();
			 * System.out.println(sublist.item(0).getNodeValue());
			 */

		}

	}
}
