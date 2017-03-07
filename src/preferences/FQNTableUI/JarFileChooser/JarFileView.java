package preferences.FQNTableUI.JarFileChooser;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ui.JBImageIcon;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

/**
 * Created by macbook on 30.12.16.
 */
public class JarFileView extends FileView {

    protected static Icon JAR_ICON = AllIcons.FileTypes.Archive;

    public String getName(File f)
    {
        String name = f.getName();
        return name.equals(" ") ? f.getPath() : name;
    }

    public String getDescription(File f)
    {
        return getTypeDescription(f);
    }
    public String getTypeDescription(File f)
    {
        String name = f.getName().toLowerCase();
        if(name.endsWith(".jar"))
            return "Java Archive File";
        else
            return "File";
    }
    public Icon getIcon(File f)
    {
        String name = f.getName().toLowerCase();
        if(name.endsWith(".jar"))
            return JAR_ICON;
        else
             return null;
    }
    public Boolean isTraversable(File f)
    {
        return ( f.isDirectory() ? Boolean.TRUE : Boolean.FALSE);
    }
}
