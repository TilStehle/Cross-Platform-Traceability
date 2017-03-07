package actions;

import com.intellij.ide.util.scopeChooser.ScopeChooserCombo;
import com.intellij.ide.util.scopeChooser.ScopeChooserConfigurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.search.scope.packageSet.NamedScope;
import com.intellij.refactoring.ui.JavaComboBoxVisibilityPanel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;
import preferences.IconProvider;
import preferences.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gerrit Greiert on 16.11.16.
 */
public class ScopeSelectionDialog extends DialogWrapper {

    private static final int rowsToDisplay = 10;

    private Set<NamedScope> scopes;
    private JBList scopeList;

    public ScopeSelectionDialog(@Nullable Project project, Set<NamedScope> scopes){
        super(project);
        this.scopes = scopes;
        init();
    }

    @Nullable
    @Override
    public JComponent createCenterPanel() {

        JPanel selectionPanel = new JPanel(new BorderLayout(5,5));
        JLabel selectionLabel = new JLabel("Select scopes to convert:");
        selectionPanel.add(selectionLabel, BorderLayout.NORTH);

        DefaultListModel<NamedScope> scopeListModel = new DefaultListModel<NamedScope>();
        for (NamedScope scope: scopes) {
            scopeListModel.addElement(scope);
        }

        scopeList = new JBList(scopeListModel);
        scopeList.setCellRenderer(new ScopeListCellRenderer());
        scopeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scopeList.setVisibleRowCount(rowsToDisplay);
        scopeList.setSelectedIndex(0);

        JScrollPane selectionScrollPane = new JBScrollPane(scopeList);
        selectionPanel.add(selectionScrollPane, BorderLayout.CENTER);

        return selectionPanel;
    }

    public Set<NamedScope> getSelectedScopes(){

        Set<NamedScope> scopes = new HashSet<NamedScope>(scopeList.getSelectedValuesList());

        return scopes;
    }
}

class ScopeListCellRenderer extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Component cell = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof NamedScope) {
            NamedScope scope = (NamedScope) value;
            setText(scope.getName());
            setToolTipText(scope.getValue().getText());
        }
        return cell;
    }
}
