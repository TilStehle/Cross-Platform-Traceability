package preferences.ExtensionUI;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.ExtensionDO;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.J2SwiftConfigWriter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import preferences.ExtensionUI.XmlEditor.XmlViewer;
import preferences.ExtensionUI.XmlFileChooser.XmlFileView;
import preferences.FQNTableUI.JarFileChooser.JarFileChooser;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;
import preferences.SimpleFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Nils-Hendrik Berger on 03.01.17.
 */
public class ExtensionSettings extends JFrame implements Configurable {

    private JComponent component;
    private ExtensionSettingsUI ui;
    private XmlViewer viewer;

    private SimpleFilter xmlFilter;
    private XmlFileView view;

    public ExtensionSettings() {
        ui = new ExtensionSettingsUI();
        setNewComponent(ui.getPanel());

        xmlFilter = new SimpleFilter("xml","XML Files");
        view = new XmlFileView();

        ui.getPanel().add(initToolbarActionsLogic(), BorderLayout.CENTER);
        setAlwaysOnTop(false);

       // RenamingReporter.init();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return getCurrentComponent();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    private void setNewComponent(JComponent newCurrentComponent){
        component = newCurrentComponent;
    }

    private JComponent getCurrentComponent()
    {
        return component;
    }

    private JPanel initToolbarActionsLogic()
    {
        return ui.getToolDecorator().setAddAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                toolbarAdd();
            }
        }).setRemoveAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                int[] indices = ui.getJList().getSelectedIndices();
                java.util.List<ExtensionDO> extensionDOList = ui.getListModel().getElementsFromIndices(indices);
                removeExtensionsFromConfigsAndTableModel(extensionDOList);

            }
        }).setEditAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                Project project = anActionButton.getDataContext().getData(CommonDataKeys.PROJECT);

                ExtensionDO extensionDO = (ExtensionDO) ui.getJList().getSelectedValue();
                if(extensionDO !=null)
                {
                    File f = new File(extensionDO.getPath());
                    if(f != null && f.isFile())
                    {

                        if(project != null) {
                            XmlViewerDialog dialog = new XmlViewerDialog(project, f);
                            dialog.show();
                        }
                        else
                        {
                            System.out.println("Project is null");
                        }
                    }
                    return;
                }
                XmlViewerDialog viewer = new XmlViewerDialog(project);
                viewer.show();
            }
        }).disableUpDownActions().setButtonComparator("Add", "Remove", "Edit").createPanel();
    }

    private void toolbarAdd()
    {
        JarFileChooser jarFileChooser  = new JarFileChooser();
        jarFileChooser.addChoosableFileFilter(xmlFilter);
        jarFileChooser.setFileView(view);
        jarFileChooser.setMultiSelectionEnabled(false);
        jarFileChooser.setFileFilter(xmlFilter);
        FileFilter ft = jarFileChooser.getAcceptAllFileFilter();
        jarFileChooser.removeChoosableFileFilter(ft);

        try {
            String path = SettingsPropertyProvider.getConfigPath();
            File f = new File(path);
            if(!f.isDirectory())
                jarFileChooser.setCurrentDirectory(new File(f.getParent()));
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }

        jarFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

        jarFileChooser.setDialogTitle("Select Extension-XML documents");
        jarFileChooser.setMultiSelectionEnabled(true);
        jarFileChooser.setPreferredSize(new Dimension(470,450));

        if(jarFileChooser.showDialog(this, "Add Extension") != JFileChooser.APPROVE_OPTION)
            return;
        final File xmlFile = jarFileChooser .getSelectedFile();
        if(!xmlFile.exists() || !isXMLDocument(xmlFile))
            return;

        writeExtensionsToConfigsAndTableModel(xmlFile);
        final File[] selected = jarFileChooser.getSelectedFiles();
        if(selected.length == 0)
        {
            ui.getPanel().setToolTipText("No files have been selected for appending ");
            return;
        }

        writeExtensionsToConfigsAndTableModel(Arrays.asList(selected));
    }

    private boolean isXMLDocument(File f)
    {
        String name = f.getName().toLowerCase();
        return (name.endsWith(".xml"));
    }
    private void writeExtensionsToConfigsAndTableModel(final java.util.List<File> extensions)
    {
        for(File file : extensions)
        {
            writeExtensionsToConfigsAndTableModel(file);
        }
    }

    /**
     * Take the received XML-Extension and writes it to the config and updats the TableModel
     * @param file
     */
    private void writeExtensionsToConfigsAndTableModel(final File file)
    {
        try {
            ExtensionDO newExtensionDO = new ExtensionDO(file.getAbsolutePath());
            J2SwiftConfigWriter.addDataObject(newExtensionDO,getSafeConfigPath());

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    DynamicListModel listModel = (DynamicListModel) ui.getListModel();
                    listModel.setNewContent(ui.loadData());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the given Extensions from the config-file and updates the listmodel
     * @param extensions
     */
    private void removeExtensionsFromConfigsAndTableModel(final java.util.List<ExtensionDO> extensions)
    {
        J2SwiftConfigWriter writer = J2SwiftConfigWriter.getInstance();

            for(ExtensionDO extension : extensions)
            {
                try {
                    writer.deleteLineFromConfigFile(extension,getSafeConfigPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ((DynamicListModel)ui.getListModel()).setNewContent(ui.loadData());
                }
            });
    }

    /**
     * Returns the config Path for the J2SwiftConfigReader and J2SwiftConfigWriter
     * @return
     */
    private static String getSafeConfigPath()
    {
        String path = "";
        try {
            path = SettingsPropertyProvider.getConfigPath();
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }
        return path;
    }

}
