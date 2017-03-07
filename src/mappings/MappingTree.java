package mappings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ui.treeStructure.Tree;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Gerrit Greiert on 19.10.16.
 */
public class MappingTree extends Tree {

    private DefaultTreeModel model;

    public MappingTree() throws FileNotFoundException, MissingPropertyException {
        super();

        String configFilePath = SettingsPropertyProvider.getConfigPath();
        MappingsReader reader = new MappingsReader(new File(configFilePath));

        model = reader.getMappingTreeModel();
        this.setModel(model);
        this.addMouseListener(new MappingTreeMouseListener(this));

    }
}