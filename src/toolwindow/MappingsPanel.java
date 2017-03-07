package toolwindow;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ui.treeStructure.Tree;
import mappings.MappingTree;
import mappings.MappingTreeMouseListener;
import mappings.MappingsReader;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Gerrit Greiert on 05.10.16.
 */
public class MappingsPanel extends JScrollPane {

    public MappingsPanel() {

        try {
            MappingTree tree = new MappingTree();
            add(tree);
            setViewportView(tree);
        } catch (Exception e) {
            add(new JLabel("An error occured while trying to read mappings"));
        }

    }
}
