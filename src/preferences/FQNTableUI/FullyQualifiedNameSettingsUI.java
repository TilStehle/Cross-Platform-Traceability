package preferences.FQNTableUI;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by Nils-Hendrik Berger
 */
class FullyQualifiedNameSettingsUI extends JFrame{


    private JBPanel mainPanel;
    private JBTextField filterTextField;
    private JBTable libraryTable;
    private ToolbarDecorator decorator;
    private DynamicJarLibraryTableModel tableModel;

    /**
     * Creates the Swing UI used in the preferences for the plugin
     */
    FullyQualifiedNameSettingsUI() {
        tableModel = new DynamicJarLibraryTableModel(FullyQualifiedNameSettings.loadTableData());
        libraryTable = new JBTable(tableModel);

        //Init

        initToolbarDecorator();
        initFilterTextField();

        mainPanel = new JBPanel();
        mainPanel.setLayout(new VerticalFlowLayout(true, true));

    }

    private void initToolbarDecorator() { decorator = ToolbarDecorator.createDecorator(libraryTable);}


    private void initFilterTextField()
    {
        //Sorting and Filter initialising
        final TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<>(getLibraryTable().getModel());
        ContainsFilter containsFilter = new ContainsFilter("");
        tableRowSorter.setRowFilter(containsFilter);
        libraryTable.setRowSorter(tableRowSorter);

        //Filter entry

        filterTextField = new HintJBTextField("Put search input here");
        filterTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                containsFilter.setFilterValue(filterTextField.getText());
                tableRowSorter.sort();
            }
        });
    }



    /**
     * Returns the JPanel containing the preferences UI
     * @return
     */
    public JPanel getPanel() {
        return mainPanel;
    }
    public JBTable getLibraryTable() {
        return libraryTable;
    }
    public JBTextField getJbTextField() {
        return filterTextField;
    }
    public DynamicJarLibraryTableModel getTableModel() { return tableModel;}
    public ToolbarDecorator getToolbarDecorator(){return decorator;}

}

