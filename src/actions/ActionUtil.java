package actions;

import iOS_Assets.ImageValidator;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

/**
 * Created by macbook on 30.11.16.
 */
public class ActionUtil {

    private ActionUtil(){}

    public static boolean isDirectoryAndContainsImages(final String path)
    {
        MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();

        ImageValidator validator = new ImageValidator();
        boolean containsImages = false;
        File javaFile = new File(path);
        if(javaFile.isDirectory())
        {   File[] fileliste= javaFile.listFiles();
            for(File file : fileliste)
            {
                if(file.isDirectory() && (isDirectoryAndContainsImages(file.getAbsolutePath())|| isImage(file)))
                {
                    containsImages = true;
                }
                if(validator.validate(file.getName()))
                {
                    containsImages = true;
                }

            }
        }
        return javaFile.isDirectory() && containsImages;
    }
    public static boolean isImage(final File imageFile)
    {
       return isImage(imageFile.getName());

    }
    public static boolean isImage(final String imageFileName)
    {
        ImageValidator validator = new ImageValidator();
        return validator.validate(imageFileName);
    }

}
