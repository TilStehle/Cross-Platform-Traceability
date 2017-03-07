package preferences.FQNTableUI;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;

import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.TableCellEditorWithButton;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.J2SwiftConfigReader;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.J2SwiftConfigWriter;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.RequiredLibraryDO;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import preferences.FQNTableUI.JarFileChooser.JarFileChooser;
import preferences.FQNTableUI.JarFileChooser.JarFileView;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;
import preferences.SimpleFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nils-Hendrik Berger on 06.12.16.
 */
public class FullyQualifiedNameSettings implements Configurable {

    private FullyQualifiedNameSettingsUI ui;
    private PropertiesComponent propertiesComponent;
    private boolean isModified = false;
    //private Table
    private String configPath;
    private SimpleFilter jarFilter;
    private JarFileView view;
    private File currentDir;
    private File destinationDir;

    public FullyQualifiedNameSettings() throws MissingPropertyException {
        ui = new FullyQualifiedNameSettingsUI();
        jarFilter = new SimpleFilter("jar","JAR Files");
        view = new JarFileView();

        currentDir = initCurrentDir();
        destinationDir = initCurrentDir();

        initToolbarActions();
        initJBTable();

        ui.getPanel().add(ui.getJbTextField(), BorderLayout.NORTH);
        ui.getPanel().add(new JBScrollPane(ui.getLibraryTable()), BorderLayout.CENTER);
        ui.getPanel().add(initToolbarActions());
        //ui.getLibraryTable().setCellEditor(new TableCellEditorWithButton());


        propertiesComponent = PropertiesComponent.getInstance();
        configPath = SettingsPropertyProvider.getConfigPath();

    }



    @Nls
    @Override
    public String getDisplayName() {
        return "J2Swift preferences.Settings";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return ui.getPanel();
    }



    /**
     * Returns true if the user has modified any preferences
     * @return
     */
    @Override
    public boolean isModified() {
        return isModified;
    }

    /**
     * Applies the modified preferences and saves them in the PropertiesComponent
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        //String newFilePath = ui.getPathTextField().getText();
        //propertiesComponent.setValue("J2Swift_targetPath", newFilePath);
        //initialTargetPath = newFilePath;

        //String newConfigFilePath = ui.getConfigFilePathTextField().getText();
        //propertiesComponent.setValue("J2Swift_configFilePath", newConfigFilePath);
        //initialConfigFilePath = newConfigFilePath;
       //ServiceManager.getService(J2SwiftService.class).updateConfigFilePath(newConfigFilePath);
    }

    /**
     * Resets modified preferences to the initial state
     */
    @Override
    public void reset() {
        //ui.getPathTextField().setText(initialTargetPath);
        //ui.getConfigFilePathTextField().setText(initialConfigFilePath);
    }

    @Override
    public void disposeUIResources() {

    }
    public static List<RequiredLibraryDO> loadTableData(){
        List<RequiredLibraryDO> libraries = J2SwiftConfigReader.getInstance().getRequiredLibraries(getSafeConfigPath());

        return libraries;
    }
    private JPanel initToolbarActions()
    {
       return ui.getToolbarDecorator().setAddAction(new AnActionButtonRunnable() {
        @Override
        public void run(AnActionButton anActionButton) {
            selectJar();
        }
    }).setRemoveAction(new AnActionButtonRunnable() {
        @Override
        public void run(AnActionButton anActionButton) {
            int[] indices = ui.getLibraryTable().getSelectedRows();
            System.out.println("Rows: " + Arrays.toString(ui.getLibraryTable().getSelectedRows()));
            java.util.List<RequiredLibraryDO> libraryDOList = ((JarLibraryTableModel) ui.getTableModel()).getElementsFromIndices(indices);
            System.out.print("List: " + libraryDOList.toString());
            removeRequiredLibraryFromConfigsAndTableModel(libraryDOList);
        }

    }).setEditAction(new AnActionButtonRunnable() {
        @Override
        public void run(AnActionButton anActionButton) {
            ((DynamicJarLibraryTableModel)ui.getTableModel()).setEditable(true);


            List<RequiredLibraryDO> libraryDOs = ((JarLibraryTableModel)ui.getTableModel()).getElementsFromIndices(ui.getLibraryTable().getSelectedColumns());
            for(RequiredLibraryDO libraryDO: libraryDOs)
            {
                //try {
                    // TODO
                    // J2SwiftConfigWriter.deleteLineFromConfigFile(new RequiredLibraryDO.DeletionBuilder(li),getSafeConfigPath());
                //} catch (IOException e) {
                  //  e.printStackTrace();
                //}
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ((DynamicJarLibraryTableModel)ui.getTableModel()).setNewContent(loadTableData());

                }
            });

        }
    }).disableUpDownActions().setButtonComparator("Add", "Remove", "Edit").createPanel();
    }

    private void initJBTable() {
        //ui.getTableModel().setNewContent(loadTableData());
       // ui.getTableModel().addTableModelListener(new JarLibraryTableModelModificationReporter());
        initColumnWidths(ui.getLibraryTable());
    }

    /**
     * Deletes the libary in the Config-File
     * @param libaryDO
     * @throws IOException
     */
    public void deleteJarColumn(final RequiredLibraryDO libaryDO) throws IOException {
        J2SwiftConfigWriter writer = new J2SwiftConfigWriter().getInstance();
        writer.deleteLineFromConfigFile(libaryDO,getSafeConfigPath());
    }

    private void selectJar()
    {
        JarFileChooser jarFileChooser  = new JarFileChooser();
        jarFileChooser.addChoosableFileFilter(jarFilter);
        jarFileChooser.setFileView(view);
        jarFileChooser.setMultiSelectionEnabled(false);
        jarFileChooser.setFileFilter(jarFilter);
        FileFilter ft = jarFileChooser.getAcceptAllFileFilter();
        jarFileChooser.removeChoosableFileFilter(ft);

        jarFileChooser.setCurrentDirectory(currentDir);
        jarFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

        jarFileChooser.setDialogTitle("Select Jar-Archive for FQN");
        jarFileChooser.setMultiSelectionEnabled(true);
        jarFileChooser.setPreferredSize(new Dimension(470,450));


        if(jarFileChooser.showDialog(ui, "Add Jar Directory") != JFileChooser.APPROVE_OPTION)
            return;

        currentDir = jarFileChooser.getCurrentDirectory();
        final File archiveFile = jarFileChooser .getSelectedFile();
        if(!archiveFile.exists() || !isArchiveFile(archiveFile))
            return;

        final File[] selected = jarFileChooser.getSelectedFiles();
        if(selected.length == 0)
        {
            JOptionPane.showMessageDialog(ui,"No JAR's seletecd","Error",JOptionPane.OK_OPTION);
            return;
        }
        java.util.List<RequiredLibraryDO> doList = FullyQualifiedNameSettings.convertFilesToRequiredLibraryDOList(selected);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ((DynamicJarLibraryTableModel)ui.getTableModel()).addEntries(doList);
            }
        });

        for(RequiredLibraryDO libraryDO: doList)
        {
            try {
                J2SwiftConfigWriter.addDataObject(libraryDO,getSafeConfigPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Removes the given Extensions from the config-file and updates the listmodel
     * @param requiredLibraryDOs
     */
    private void removeRequiredLibraryFromConfigsAndTableModel(final java.util.List<RequiredLibraryDO> requiredLibraryDOs)
    {

        J2SwiftConfigWriter writer = J2SwiftConfigWriter.getInstance();

        for(RequiredLibraryDO requiredLibraryDO : requiredLibraryDOs)
        {
            try {
                writer.deleteLineFromConfigFile(requiredLibraryDO,getSafeConfigPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                (ui.getTableModel()).setNewContent(loadTableData());
            }
        });
    }


    private boolean isArchiveFile(File f)
    {
        String name = f.getName().toLowerCase();
        return (name.endsWith(".jar"));
    }

    private File initCurrentDir()
    {
        try {
            return new File(SettingsPropertyProvider.getConfigPath());
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }
        //Location if error was thrown
        return new File(System.getProperty("user.dir"));

    }



    private static void initColumnWidths(final JBTable table)
    {
        final TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(200);
        tableColumnModel.getColumn(1).setPreferredWidth(50);
    }

    public static List<RequiredLibraryDO> convertFilesToRequiredLibraryDOList(File[] files)
    {
        List<RequiredLibraryDO> doList = new ArrayList<>();
        for(File file :files)
        {
            doList.add(new RequiredLibraryDO(file.getAbsolutePath() ," "));
        }
        return doList;
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
