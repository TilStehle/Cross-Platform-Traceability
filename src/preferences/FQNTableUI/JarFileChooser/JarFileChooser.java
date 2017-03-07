package preferences.FQNTableUI.JarFileChooser;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import preferences.TabListCellRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Nils-Hendrik Berger on 30.12.16.
 */
public class JarFileChooser extends JFileChooser{
    protected JBList jarEntries;

    protected JDialog createDialog(Component parent) throws HeadlessException
    {
        JDialog dialog = super.createDialog(parent);

        jarEntries = new JBList();
        jarEntries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        TabListCellRenderer renderer = new TabListCellRenderer();
        renderer.setTabs(new int[] {240 ,300 ,360});
        jarEntries.setCellRenderer(renderer);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(0, 10, 10, 10));
        p.add(new JBLabel("Jar Archives to Add: "), BorderLayout.SOUTH);

        JBScrollPane ps = new JBScrollPane(jarEntries);
        p.add(ps, BorderLayout.CENTER);
        dialog.getContentPane().add(p, BorderLayout.SOUTH);
        PropertyChangeListener lst = new PropertyChangeListener() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            DefaultListModel emptyModel = new DefaultListModel();
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName() == JarFileChooser.FILE_FILTER_CHANGED_PROPERTY)
                {
                    jarEntries.setModel(emptyModel);
                    return;
                }
                else if(evt.getPropertyName() == JarFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
                {
                    File f = getSelectedFile();
                    if(f == null)
                    {
                        jarEntries.setModel(emptyModel);
                        return;
                    }
                    String name = f.getName().toLowerCase();
                    if(!name.endsWith(".jar"))
                    {
                        jarEntries.setModel(emptyModel);
                        return;
                    }
                    try
                    {
                        ZipFile jarFile = new ZipFile(f.getPath());
                        DefaultListModel model = new DefaultListModel();
                        Enumeration en = jarFile.entries();
                        while(en.hasMoreElements())
                        {
                            ZipEntry jarEntr= (ZipEntry) en.nextElement();
                            Date d = new Date(jarEntr.getTime());
                            String str = jarEntr.getName()+ "\t" + simpleDateFormat.format(d);
                            model.addElement(str);
                        }
                        jarFile.close();
                        jarEntries.setModel(model);
                        jarEntries.setSelectionInterval(0,model.getSize()-1);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
                else{
                    jarEntries.setModel(emptyModel);
                    return;
                }
            }
        };
        addPropertyChangeListener(lst);
        cancelSelection();

        return dialog;
    }
    public String[] getSelectedEntries()
    {
        List selObj = jarEntries.getSelectedValuesList();
        String[] entries = new String[selObj.size()];
        for(int k = 0; k < selObj.size(); k++)
        {
            String str = selObj.get(k).toString();
            int index = str.indexOf("\t");
            entries[k] = str.substring(0, index);
        }
        return entries;
    }
}
