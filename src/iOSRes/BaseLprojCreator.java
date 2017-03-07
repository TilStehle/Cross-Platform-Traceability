package iOSRes;
import java.io.File;

/**
 * Creates a the Base.lprof Directory
 * @author Nils-Hendrik Berger *
 */
public class BaseLprojCreator {
	public static final String FILENAME = "Base.lproj";
	/**
	 * Only creates a new directory if it does not exists.
	 * @param destination
	 */
	public void createDirectory(final File destination)
	{
		File base = new File(destination.getPath() +"/" + FILENAME);
		if(base.exists())
		{
			System.out.println(FILENAME + "does already exist! No creation of directory!");
			
		}
		else
		{
			base.mkdirs();
		}
		
	}
	
	public boolean doesBaseDirExistInDirectory(final File directory, final String name)
	{
		final File[] dirContent = directory.listFiles(new FileFilterUtils.DirFileFilter());
		for(File file: dirContent)
		{
			if(file.getPath().equals(directory.getPath() +"/name"))
			{
				return true;
			}

		}
		return false;
	}

}
