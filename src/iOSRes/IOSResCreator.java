package iOSRes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;


/**
 * @author Nils-Hendrik Berger
 *
 */
public class IOSResCreator {
	final private File _destination;
	final private XMLLocalizableParser _lscreator;
	final private RSwiftCreator _rswiftcreator;
	
	
	/**
	 * 
	 * @throws FileNotFoundException 
	 * @throws ParserConfigurationException 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public IOSResCreator(final File destination) throws FileNotFoundException, ParserConfigurationException {
		
		_destination = destination;		
		
		_lscreator = new XMLLocalizableParser(_destination);
		_rswiftcreator = new RSwiftCreator(_destination);
		
	}
	
//	/**
//	 * Transforms XML-Files in the Array to IOS-Resources
//	 * @param xmlFiles
//	 */
//	public void createResources(VirtualFile[] xmlFiles)
//	{
//		//TODO Convert VirtualFile to File
//		//createResources(xmlFiles);
//	}
	/**
	 * Transforms all the XML-Files in List IOS-Resources
	 * @param xmlFiles
	 */
	public void createResources(final List<File> xmlFiles)
	{ 
		setUpResources();
		_rswiftcreator.parseFiles(xmlFiles);
		_lscreator.parseFiles(xmlFiles);	
		
		
	}
	/**
	 * Transforms all the Android-Resources in the given directory to IOS-Resources
	 * @param sourceDirectory
	 */
	public void createResources(final File sourceDirectory){
		//Setting up directory
		setUpResources();
		//Get all XML-Files in the given sourcDirectory
		ExtensionFilterVisitor xmlVisitor = new ExtensionFilterVisitor("xml");
		try {
			Files.walkFileTree(Paths.get(sourceDirectory.getPath()), xmlVisitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Path> xmlPathFiles = xmlVisitor.getResults();		
		//Convert Path to File
		List<File> xmlFiles2 = xmlPathFiles.stream().map(Path::toFile).collect(Collectors.toList());
		
		_rswiftcreator.parseFiles(xmlFiles2);
		_lscreator.parseFiles(xmlFiles2);		
	}
	
	 /**
	  * Sets up the folder structure
	  */
	private void setUpResources()
	{
		BaseLprojCreator baseCreator = new BaseLprojCreator();
		baseCreator.createDirectory(_destination);		
	}

}
