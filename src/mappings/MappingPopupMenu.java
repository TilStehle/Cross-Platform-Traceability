package mappings;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.IconLoader;
import preferences.IconProvider;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Gerrit Greiert on 17.10.16.
 */
public class MappingPopupMenu extends JPopupMenu {

    private TreePath path;

    public MappingPopupMenu(TreePath path) {

        this.path = path;
        if (this.path.getPathCount() > 1) {

            MappingFileNode selectedMappingFileNode = (MappingFileNode) this.path.getPathComponent(1);

            JMenuItem addMenuItem = new JMenuItem("Add mapping", IconProvider.MAPPING_ADD);
            addMenuItem.addActionListener(new MappingAddListener(selectedMappingFileNode));
            add(addMenuItem);

            if (this.path.getLastPathComponent() instanceof MappingNode) {

                MappingNode selectedMappingNode = (MappingNode) this.path.getLastPathComponent();
                JMenuItem editMenuItem = new JMenuItem("Edit mapping", IconProvider.MAPPING_EDIT);
                editMenuItem.addActionListener(new MappingEditListener(selectedMappingFileNode, selectedMappingNode));
                add(editMenuItem);

                JMenuItem deleteMenuItem = new JMenuItem("Delete mapping", IconProvider.MAPPING_DELETE);
                deleteMenuItem.addActionListener(new MappingDeleteListener(selectedMappingFileNode, selectedMappingNode));
                add(deleteMenuItem);
            }
        }
    }
}

class MappingAddListener implements ActionListener {

    private MappingFileNode mappingFileNode;

    public MappingAddListener(MappingFileNode mappingFileNode) {
        this.mappingFileNode = mappingFileNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        MappingEditorDialog editorDialog = new MappingEditorDialog(null);

        editorDialog.show();

        if (editorDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            MappingNode mapping = new MappingNode(editorDialog.getMappingType(), editorDialog.getJavaString(), editorDialog.getSwiftString(), editorDialog.getNoGen());
            mappingFileNode.addMapping(mapping);
        }
    }
}

class MappingEditListener implements ActionListener{

    private MappingFileNode mappingFileNode;
    private MappingNode mappingNode;

    public MappingEditListener(MappingFileNode mappingFileNode, MappingNode mappingNode) {
        this.mappingFileNode = mappingFileNode;
        this.mappingNode = mappingNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        MappingEditorDialog editorDialog = new MappingEditorDialog(null, mappingNode);

        editorDialog.show();

        if (editorDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            mappingFileNode.editMapping(mappingNode, editorDialog.getUpdatedMapping());
        }
    }
}

class MappingDeleteListener implements ActionListener{

    private MappingFileNode mappingFileNode;
    private MappingNode mappingNode;

    public MappingDeleteListener(MappingFileNode mappingFileNode, MappingNode mappingNode) {
        this.mappingFileNode = mappingFileNode;
        this.mappingNode = mappingNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        mappingFileNode.deleteMapping(mappingNode);
    }
}
