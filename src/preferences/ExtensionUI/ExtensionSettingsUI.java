package preferences.ExtensionUI;

import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.ExtensionDO;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.J2SwiftConfigReader;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by macbook on 03.01.17.
 */
public class ExtensionSettingsUI {
    private JBPanel panel;
    private JList list;
    private ToolbarDecorator decorator;
    private DynamicListModel listModel;


    public ExtensionSettingsUI() {
        this.panel = new JBPanel(new BorderLayout());

        setupUI();
        initToolbarDecorator();
    }

    public void setupUI()
    {
        final java.util.List<ExtensionDO> extensions = loadData();
        listModel = new DynamicListModel(extensions);

        list = new JBList(listModel);
        list.setCellRenderer(new ExtensionListCellRenderer());


        panel.add(new JBScrollPane(list), BorderLayout.LINE_START);
        TitledBorder border = BorderFactory.createTitledBorder("Extensions");
        panel.setBorder(border);


    }
    private void initToolbarDecorator()
    {
        decorator = ToolbarDecorator.createDecorator(list);
    }

    public List<ExtensionDO> loadData()
    {
        try {
            String configPath = SettingsPropertyProvider.getConfigPath();
            J2SwiftConfigReader reader = J2SwiftConfigReader.getInstance();
            return reader.getExtenstions(configPath);
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public JBPanel getPanel(){return panel;}
    public ToolbarDecorator getToolDecorator(){return decorator;}
    public JList getJList(){return list;}
    public ExtensionListModel getListModel(){return listModel;}

}
