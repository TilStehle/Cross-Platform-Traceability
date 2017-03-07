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

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class RSwiftCreator {
	public static final String FILENAME_RSWIFT = "R.swift";
	public static final String FILENAME_CONTEXT_ADAPTER = "ContextAdapter";
	public static final String FILEENDING = ".swift";
	
	//Holds the content from the XML-Document. Key --> node.getTextContent(), Value --> node.getNodeValue().
	//The TreeMap is needed to print the content in an alphabetical order.
	private static SortedMap<String, String> _stringXmlContent= new TreeMap<>();
	private static SortedMap<String, ArrayList<String>> _stringArrayXmlContent = new TreeMap<>();
	private static SortedMap<String, HashMap<String,String>> _pluralsXmlContent = new TreeMap<>();
	
	private static PrintWriter _printWriter;
	private DocumentBuilder _documentBuilder;
	private final File _destination;
	
	public RSwiftCreator(final File destination) throws FileNotFoundException, ParserConfigurationException
	{
		this._destination = destination;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		_documentBuilder = factory.newDocumentBuilder();
		_printWriter = new PrintWriter(_destination.getPath() +"/" + FILENAME_RSWIFT);
		
	}
	
	public void parseFiles(final List<File> xmlFiles)
	{
		for(File file: xmlFiles)
		{
			parseXML(file);
		}
		//Print to file
		printRSwift();
		
		//Closing the printer
		createContextAdapter(_destination);
		_printWriter.flush();
		_printWriter.close();
		clearMaps();
	}	
	
	/**
	 * Runs through the XML-Document and creates the "case" part
	 * @param xmlFile 
	 * @param printer
	 */
	private void parseXML(final File xmlFile){
		Document document;
		try {
			document = _documentBuilder.parse(xmlFile);
			parseStringResources(document);
			parseStringArrayResources(document);
			parsePluralsResource(document);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}	

	}
	private void printRSwift()
	{
		printBeginning();
		printBeginningStructR();
		printStringContent();
		printStringArrayContent();
		printPluralsContent();
		printEndingStructT();	
		printAccessOperators();
			
	}
	
	/**
	 * 
	 */
	private void printBeginning()
	{
		_printWriter.println("// THIS FILE WAS CREATED BY J2SWIFT, PLEASE DO NOT EDIT");
		_printWriter.println("");
		_printWriter.println("import Foundation");
		_printWriter.println("");	
	}
	
	/**
	 * 
	 */
	private void printBeginningStructR() {
		_printWriter.println("public struct R {");
		_printWriter.println("");	
		
	}
	/**
	 * 
	 */
	private void printAccessOperators() {
		//Check if enum string is needed
		if(!_stringXmlContent.isEmpty())
		{
			_printWriter.println("");
			_printWriter.println("postfix operator ^ {}");
			_printWriter.println("");
			_printWriter.println("postfix func ^ (key: R.string) -> String {");
			_printWriter.println("\t return NSLocalizedString(key.rawValue, comment: \"\")");
			_printWriter.println("}");
			_printWriter.println("");
		}
		if(!_stringArrayXmlContent.isEmpty())
		{
			_printWriter.println("postfix func ^ (key: R.array) -> [String] {");
			_printWriter.println("\treturn R.arrays[key.rawValue]! as! [String]");
			_printWriter.println("}");
		}
		
	}
	
	/**
	 * 
	 */
	private void printEndingStructT() {
		_printWriter.println("}");		
	}

	/**
	 * Runs over the nodes and stores "textContent" and "nodeValues" in the Map
	 * @param childNodes
	 * @param printerWriter
	 */
	private void parseStringResources(final Document document) {
			
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
	private void parseStringArrayResources(final Document document) {
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
	private void printStringArrayContent(){
		_printWriter.println("");
		_printWriter.println("\tpublic enum array : String {");
		for (Entry<String, ArrayList<String>> entry : _stringArrayXmlContent.entrySet()) {
			
			_printWriter.println("\t\tcase " + entry.getKey());
					
		}
		_printWriter.println("");
		_printWriter.println("\t\tsubscript(index: Int) -> String {");
		_printWriter.println("\t\t\treturn R.arrays[self.rawValue]![index] as! String");
		_printWriter.println("\t\t}");
		_printWriter.println("\t}");
		_printWriter.println();	
		_printWriter.println("\tprivate static var arrays: NSDictionary = {");
		_printWriter.println("\t\tlet path = NSBundle.mainBundle().pathForResource(\"LocalizableArray\", ofType: \"strings\")!");
		_printWriter.println("\t\treturn NSDictionary(contentsOfFile: path)!");
		_printWriter.println("\t}()");
		_printWriter.println("");
	}
	
	/**
	 * Iterates over the map and prints out the content
	 * @param printerWriter
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void printStringContent(){
		Set<Entry<String, String>> set = _stringXmlContent.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		_printWriter.println("\t public enum string : String {");
		while (iterator.hasNext()) {
			Map.Entry<String, String> mentry = (Map.Entry<String, String>) iterator.next();
			_printWriter.println("\t \t /// " + mentry.getValue());
			_printWriter.println("\t \t case " +mentry.getKey());
			//_printWriter.println("\""+mentry.getKey() + "\"" + " = " + "\""+ mentry.getValue()+ "\"");
			
		}		
		_printWriter.println("\t}");		
	}
	
	/**
	 * Iterates over the map and prints out the content
	 * @param printerWriter
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void printPluralsContent(){
		_printWriter.println("\tpublic enum plurals : String {");
		for (Entry<String, HashMap<String,String>> entry : _pluralsXmlContent.entrySet()) {
			_printWriter.println("\t\tcase " + entry.getKey());
			}
		_printWriter.println("");
		_printWriter.println("\t\tsubscript(quantity: Int) -> String {");
		_printWriter.println("\t\t\treturn String.localizedStringWithFormat(NSLocalizedString(self.rawValue, comment: \"\"), quantity)");
		_printWriter.println("\t\t}");
		_printWriter.println("\t}");
		
	}
	/**
	 * Creates the class ContextAdapter that provides android-like access for the iOS-Project. Only creates the needed access methods.
	 * @param destination
	 */
	private void createContextAdapter(final File destination)
	{
		try {
			PrintWriter pw = new PrintWriter(destination.getAbsolutePath() + "/" + FILENAME_CONTEXT_ADAPTER + FILEENDING, "UTF-8");
			
			//File Beginning
			pw.println("//Class created by J2Swift");
			pw.println();
			pw.println("import Foundation");
			pw.println("");
			pw.println("public class " +FILENAME_CONTEXT_ADAPTER);
			
			pw.println("{");
			pw.println("");
			
			//File Ending
			if(!_stringXmlContent.isEmpty())
			{
				//getString
				pw.println("\t/**");
				pw.println("\t*Gives back the String located in R.swift");
				pw.println("\t*/");
				pw.println("\tpublic func getString(string:R.string)->String");
				pw.println("\t{");
				pw.println("\t\treturn(string^)");
				pw.println("\t}");
			}
			if(!_stringArrayXmlContent.isEmpty())
			{
				//getStringArray
				pw.println("\t/**");
				pw.println("\t*Gives back the Array located in R.swift");
				pw.println("\t*/");
				pw.println("\tpublic func getStringArray(arrayString:R.array)->[String]");
				pw.println("\t{");
				pw.println("\t\treturn(arrayString^)");
				pw.println("\t}");	
				
				//getStringArrayItem
				pw.println("\t/**");
				pw.println("\t*Gives back the Item");
				pw.println("\t*/");
				pw.println("\tpublic func getStringArrayItem(arrayStringItem:R.array, position:Int)->String");
				pw.println("\t{");
				pw.println("\t\treturn(arrayStringItem[position])");
				pw.println("\t}");
			}
			if(!_pluralsXmlContent.isEmpty())
			{
				//getStringPlurals
				pw.println("\t/**");
				pw.println("\t*Gives back the plurals located in R.swift");
				pw.println("\t*/");
				pw.println("\tpublic func getStringPlurals(plurals:R.plurals,position:Int)->String");
				pw.println("\t{");
				pw.println("\t\treturn(plurals[position])");
				pw.println("\t}");	
			}
			
			//Closing
			pw.println("}");
			 
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
