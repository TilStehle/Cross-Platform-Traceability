package iOSRes;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


/**
 * Stores all the files that match the imageExtension
 * @author Nils-Hendrik Berger *
 */
public class ExtensionFilterVisitor extends SimpleFileVisitor<Path>{
	private final String _imageExtension;
	private final List<Path> result = new ArrayList<Path>();
	
	public ExtensionFilterVisitor(final String imageExtension) {
		_imageExtension = imageExtension;
	}

	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs){	
			final Path filePath = file.getFileName();
			if(filePath.toString().toLowerCase().endsWith(_imageExtension))
			{
				System.out.println("Add file:" + file);
				result.add(file);
			}
			
			//Continue processing
			return FileVisitResult.CONTINUE;		
	}
	public List<Path> getResults()
	{
		return new ArrayList<Path>(result);
	}

}
