package iOSRes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLLocalizableParser {
	public static final String FILENAME_STRING = "Localizable.strings";
	public static final String FILENAME_STRING_ARRAY = "LocalizableArray.strings";
	public static final String FILENAME_PLURALS =  "Localizable.stringsdict";
	
	//Holds the content from the XML-Document. Key --> node.getTextContent(), Value --> node.getNodeValue().
	//The TreeMap is needed to print the content in an alphabetical order.
	private static SortedMap<String, String> _stringXmlContent = new TreeMap<>();
	private static SortedMap<String, ArrayList<String>> _stringArrayXmlContent = new TreeMap<>();
	private static SortedMap<String, HashMap<String,String>> _pluralsXmlContent = new TreeMap<>();
	
	private DocumentBuilder _documentBuilder;
	private final File _destination;
	
	public XMLLocalizableParser(final File destination) throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		_documentBuilder = factory.newDocumentBuilder();
		_destination = new File(destination.getPath() + "/" +  "Base.lproj");
		
	}
	/**
	 * Parses every XML-File and transforms the given content 
	 * @param xmlFiles
	 */
	public void parseFiles(final List<File> xmlFiles)
	{
		for(File file: xmlFiles)
		{
			parseXML(file);
		}
		//Print to file
		try {
			printStringContent();
			printStringArrayContent();
			printPluralsContent();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		clearMaps();
	}	
	
	/**
	 * Runs through the XML-Document and creates the "case" part
	 * @param xmlFile 
	 * @param printer
	 */
	private void parseXML(File xmlFile) {

			Document document;
			try {
				document = _documentBuilder.parse(xmlFile);
				System.out.println("Parsing file : " + xmlFile.getName());
				
				//Parse the document
				parseStringResources(document);
				parseStringArrayResources(document);
				parsePluralsResource(document);					
			} catch (SAXException | IOException e) {
				e.printStackTrace();
			}	
	}
	
	/**
	 * Runs over the nodes and stores "textContent" and "nodeValues" in the Map
	 * @param childNodes
	 * @param printerWriter
	 */
	private void parseStringResources(Document document) {
			
			NodeList nodeList = document.getElementsByTagName("string");
			for(int i = 0; i < nodeList.getLength(); i++){
				//Get node
				Node node = nodeList.item(i);
				
				//Get content of "string" element
				String content = node.getTextContent();
				
				//Get content of "name" element
				NamedNodeMap attributes = node.getAttributes();
				Node attributeNode = attributes.getNamedItem("name");
				String attributeValue = attributeNode.getNodeValue();
				
				//Put key/value in map
				_stringXmlContent.put(attributeValue, content);				
			}
		}
	/**
	 * Runs over the nodes and stores "item elements" and the "attributes" in the map
	 * @param document
	 */
	private void parseStringArrayResources(Document document) {
				
				// get all elements called "string-array"
				NodeList nodeList = document.getElementsByTagName("string-array");
				for (int i = 0; i < nodeList.getLength(); i++) {
					// Get node
					Node node = nodeList.item(i);

					// Get content of all "item" elements
					NodeList childNodes = node.getChildNodes();
					ArrayList<String> itemList = new ArrayList<String>();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node childNode = childNodes.item(j);
						if (childNode.getNodeName().equals("item")) {
							itemList.add(childNode.getTextContent());
						}
					}

					// Get content of "name" attribute
					NamedNodeMap attributes = node.getAttributes();
					Node attributeNode = attributes.getNamedItem("name");
					String attributeValue = attributeNode.getNodeValue();

					// Put key/value pair in map
					_stringArrayXmlContent.put(attributeValue, itemList);
				}		
	}
	
	/**
	 * 
	 * @param document
	 */
	private void parsePluralsResource(Document document)
	{
		// get all elements called "plurals"
				NodeList nodeList = document.getElementsByTagName("plurals");
				for (int i = 0; i < nodeList.getLength(); i++) {
					// Get node
					Node node = nodeList.item(i);

					// Get content of all "item" elements
					NodeList childNodes = node.getChildNodes();
					HashMap<String, String> itemMap = new HashMap<String, String>();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node childNode = childNodes.item(j);
						if (childNode.getNodeName().equals("item")) {
							NamedNodeMap attributes = childNode.getAttributes();
							Node quantityNode = attributes.getNamedItem("quantity");
							String quantityValue = quantityNode.getNodeValue();
							
							String itemValue = childNode.getTextContent();
							
							itemMap.put(quantityValue, itemValue);
						}
					}

					// Get content of "name" attribute
					NamedNodeMap attributes = node.getAttributes();
					Node attributeNode = attributes.getNamedItem("name");
					String attributeValue = attributeNode.getNodeValue();
//					System.out.println(attributeValue);
//					System.out.println();
//					itemMap.forEach((k,v)-> System.out.println(k+", "+v));

					// Put key/value pair in map
					_pluralsXmlContent.put(attributeValue, itemMap);
				}
			}
		
		
	/**
	 * Iterates over the map and prints out the content
	 * @param printerWriter
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void printStringContent() throws FileNotFoundException, UnsupportedEncodingException {
		//Adding the filename
		final File stringDestination = new File(_destination.getAbsolutePath() + "//" + FILENAME_STRING);
		
		//Printing content to the destination
		PrintWriter printerWriter = new PrintWriter(stringDestination, "UTF-8");
		Set<Entry<String, String>> set = _stringXmlContent.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> mentry = (Map.Entry<String, String>) iterator.next();
			printerWriter.println("\""+mentry.getKey() + "\"" + " = " + "\""+ mentry.getValue()+ "\";");				
		}		
		printerWriter.flush();
		printerWriter.close();
	}
	
	/**
	 * Iterates over the map and prints out the content
	 * @param printerWriter
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void printStringArrayContent() throws FileNotFoundException, UnsupportedEncodingException {
		//Adding the filename
		final File stringDestination = new File(_destination.getAbsolutePath() + "//" + FILENAME_STRING_ARRAY);
		
		//Printing content to the destination
		PrintWriter printerWriter = new PrintWriter(stringDestination, "UTF-8");
		for (Entry<String, ArrayList<String>> entry : _stringArrayXmlContent.entrySet()) {
			printerWriter.println("\"" + entry.getKey() + "\"" + " = (");
			for(String item : entry.getValue())
			{
				printerWriter.println("\t" + "\""+ item +"\",");
			}
			printerWriter.println(");");
			printerWriter.println("");
		}				
		printerWriter.flush();
		printerWriter.close();
	}
	
	/**
	 * Iterates over the map and prints out the content
	 * @param printerWriter
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void printPluralsContent() throws FileNotFoundException, UnsupportedEncodingException {
		//Adding the filename
		final File stringDestination = new File(_destination.getAbsolutePath() + "//" + FILENAME_PLURALS);
		
		//Printing content to the destination
		PrintWriter printerWriter = new PrintWriter(stringDestination, "UTF-8");
		printerWriter.println("<plist version="+  "\"" +"1.0" + "\""+">");
		printerWriter.println("<dict>");
		printerWriter.println("");
		for (Entry<String, HashMap<String,String>> entry : _pluralsXmlContent.entrySet()) {
			printerWriter.println("<key>" + entry.getKey() +  "</key>");
			printerWriter.println("<dict>");
			printerWriter.println("\t<key>NSStringLocalizedFormatKey</key>");
			printerWriter.println("\t<string>%#@x@</string>");
			printerWriter.println("\t<key>x</key>");
			printerWriter.println("\t<dict>");
			printerWriter.println("\t\t<key>NSStringFormatSpecTypeKey</key>");
			printerWriter.println("\t\t<string>NSStringPluralRuleType</string>");
			printerWriter.println("\t\t<key>NSStringFormatValueTypeKey</key>");
			printerWriter.println("\t\t<string>d</string>");
			HashMap<String, String> map = entry.getValue();
			for(Map.Entry<String,String> hashMapEntry: map.entrySet())
			{
				printerWriter.println("\t\t<key>"+ hashMapEntry.getKey()+"</key>");
				printerWriter.println("\t\t<string>"+hashMapEntry.getValue()+"</string>");
			}
			printerWriter.println("\t</dict>");
			printerWriter.println("</dict>");
			printerWriter.println("");
		}
		printerWriter.println("</dict>");
		printerWriter.println("</plist>");
		printerWriter.flush();
		printerWriter.close();
	}
	
	
	
	
	/**
	 * Deletes the content in the maps
	 */
	private void clearMaps()
	{
		_stringXmlContent.clear();
		_stringArrayXmlContent.clear();
		_pluralsXmlContent.clear();
	}
}
