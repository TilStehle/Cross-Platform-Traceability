package iOSRes;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * 
 */

/**
 * @author Nils-Hendrik Berger
 *
 */
public class FileFilterUtils {
	public static final String XML_ENDING = ".xml";
	
	public static class XmLFileFilter implements FilenameFilter
	{

		@Override
		public boolean accept(final File dir, final String fileName) {
			//OLD
			return fileName.toLowerCase().endsWith(XML_ENDING);
		}
		
	}
	public static class DirFileFilter implements FileFilter
	{

		@Override
		public boolean accept(final File pathname) {
			return pathname.isDirectory();
		}
		
	}
	
	

}
