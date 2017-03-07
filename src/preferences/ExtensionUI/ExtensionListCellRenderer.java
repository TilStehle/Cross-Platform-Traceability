package preferences.ExtensionUI;

import com.intellij.icons.AllIcons;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.ExtensionDO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.File;

/**
 * Created by Nils-Hendrik Berger on 03.01.17.
 */
public class ExtensionListCellRenderer extends DefaultListCellRenderer{

    private static final Icon extensionIcon = AllIcons.FileTypes.Xml;


    private static Border border = BorderFactory.createMatteBorder(0,1,1,1,Color.GRAY);
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();


    public ExtensionListCellRenderer() {
        setOpaque(true);

    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        renderer.setBorder(border);
        renderer.setOpaque(true);
        final ExtensionDO extensionDO = (ExtensionDO) value;
        File extension = new File(extensionDO.getPath());
        if(extension.exists()) {

            int pathTextFontSize = 2;
            String text = "<html><pre><font face=" + getFont().getName() +" ><b>" +  extension.getName() + "</b> " + "\r\n" + "<font size=" +"\"" + pathTextFontSize + "\">" + extensionDO.getPath() + "</font></font></pre></html>";
            renderer.setFont(getFont());
            renderer.setText(text);
            renderer.setIcon(extensionIcon);
        }
        return renderer;
    }
}
