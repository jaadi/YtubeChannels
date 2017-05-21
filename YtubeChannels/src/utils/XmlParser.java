package utils;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import main.Consts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entities.Channel;

public class XmlParser {
	
	private ArrayList<Channel> channelList;
	
	public XmlParser() {
		initChannelList();
	}	
	
	private void initChannelList() {
		this.channelList = getChannelsFromOpml();		
	}


	public  ArrayList<Channel> getChannelsFromOpml() {

		ArrayList<Channel> channeList = new ArrayList<Channel>();
		File opmlFile = new File(Consts.OPML_FILENAME);
		
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(opmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("outline");

			if (nList != null) {

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						String text = eElement.getAttribute("text");
						if(text.equals("YouTube-Abos")){continue;}
						String title = eElement.getAttribute("title");
						String xmlUrl = eElement.getAttribute("xmlUrl");
						String id = extractId(xmlUrl);
						channeList.add(new Channel(title,id));
					}
				}
			}			
			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		return channeList;
	}

	private static String extractId(String xmlUrl) {
		int index = xmlUrl.lastIndexOf("channel_id=");
		if(index == -1){
			return "";
		} else {		
			String sub = xmlUrl.substring( index+ "channel_id=".length());
			return sub;
		}	
	}
	
	public static ArrayList<Channel> getChannels(File opmlFile){
		ArrayList<Channel> channels = new ArrayList<>();
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(opmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("outline");

			if (nList != null) {

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						String text = eElement.getAttribute("text");
						if(text.equals("YouTube-Abos")){continue;}
						String title = eElement.getAttribute("title");
						String xmlUrl = eElement.getAttribute("xmlUrl");
						String id = extractId(xmlUrl);
						channels.add(new Channel(title,id));
					}
				}
			}			
			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		return channels;
	}
}
