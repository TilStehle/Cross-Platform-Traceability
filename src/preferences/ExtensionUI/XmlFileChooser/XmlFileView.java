package preferences.ExtensionUI.XmlFileChooser;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

/**
 * Created by Nils-Hendrik Berger on 03.01.17.
 */
public class XmlFileView extends FileView {

    protected static Icon XML_ICON = AllIcons.FileTypes.Xml;

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
        if(name.endsWith(".xml"))
            return "XML Document";
        else
            return "File";
    }
    public Icon getIcon(File f)
    {
        String name = f.getName().toLowerCase();
        if(name.endsWith(".xml"))
            return XML_ICON;
        else
            return null;
    }
    public Boolean isTraversable(File f)
    {
        return ( f.isDirectory() ? Boolean.TRUE : Boolean.FALSE);
    }
}
