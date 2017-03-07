package preferences;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 *
 * Created by Nils-Hendrik Berger on 30.12.16.
 */
public class SimpleFilter extends FileFilter {

    private String description = null;
    private String extension = null;

    public SimpleFilter(String extension, String description) {
        this.description = description;
        this.extension = "." + extension.toLowerCase();
    }

    @Override
    public boolean accept(File f) {
        if(f == null)
            return false;
        if(f.isDirectory())
            return true;
        return f.getName().toLowerCase().endsWith(extension);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
