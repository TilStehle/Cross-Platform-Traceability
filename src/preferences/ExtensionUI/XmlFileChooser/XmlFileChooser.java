package preferences.ExtensionUI.XmlFileChooser;

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
import java.util.Date;
import java.util.zip.ZipEntry;

/**
 * Created by Nils-Hendrik Berger on 03.01.17.
 */
public class XmlFileChooser extends JFileChooser {
    protected JBList xmlEntries;

    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);

        xmlEntries = new JBList();
        xmlEntries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        TabListCellRenderer renderer = new TabListCellRenderer();
        renderer.setTabs(new int[]{240, 300, 360});
        xmlEntries.setCellRenderer(renderer);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(0, 10, 10, 10));
        p.add(new JBLabel("Jar Archives to Add: "), BorderLayout.SOUTH);

        JBScrollPane ps = new JBScrollPane(xmlEntries);
        p.add(ps, BorderLayout.CENTER);
        dialog.getContentPane().add(p, BorderLayout.SOUTH);
        PropertyChangeListener lst = new PropertyChangeListener() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            DefaultListModel emptyModel = new DefaultListModel();

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == XmlFileChooser.FILE_FILTER_CHANGED_PROPERTY) {
                    xmlEntries.setModel(emptyModel);
                    return;
                } else if (evt.getPropertyName() == XmlFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
                    File f = getSelectedFile();
                    if (f == null) {
                        xmlEntries.setModel(emptyModel);
                        return;
                    }
                    String name = f.getName().toLowerCase();
                    if (!name.endsWith(".xml")) {
                        xmlEntries.setModel(emptyModel);
                        return;
                    }
                    try {
                        File xmlFile = new File(f.getPath());
                        DefaultListModel model = new DefaultListModel();
                        String str = xmlFile.getName();
                        model.addElement(str);
                        xmlEntries.setModel(model);
                        xmlEntries.setSelectionInterval(0, model.getSize() - 1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    xmlEntries.setModel(emptyModel);
                    return;
                }
            }
        };
        addPropertyChangeListener(lst);
        cancelSelection();
        return dialog;
    }

    public String[] getSelectedEntries() {
        java.util.List selObj = xmlEntries.getSelectedValuesList();
        String[] entries = new String[selObj.size()];
        for (int k = 0; k < selObj.size(); k++) {
            String str = selObj.get(k).toString();
            int index = str.indexOf("\t");
            entries[k] = str.substring(0, index);
        }
        return entries;
    }
}
