package mappings;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Gerrit Greiert on 06.10.16.
 */
public class MappingsReader {

    private ArrayList<MappingFileNode> mappingFileList;
    private File configFile;
    private DefaultTreeModel model;

    public MappingsReader(File configFile) throws FileNotFoundException {

        this.configFile = configFile;
        mappingFileList = new ArrayList<MappingFileNode>();

        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            if(parts[0].equals("#mapping")){

                MappingFileNode mappingFileNode = new MappingFileNode(new File(parts[1]));
                mappingFileList.add(mappingFileNode);
            }
        }
        scanner.close();
    }

    public DefaultTreeModel getMappingTreeModel(){

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(configFile.getName());
        DefaultTreeModel model = new DefaultTreeModel(root);
        //model.addTreeModelListener(new MappingTreeModelListener(model));

        int index = 0;

        for (MappingFileNode node: mappingFileList) {
            node.registerParentTreeModel(model);
            model.insertNodeInto(node, root, index);
            index++;
        }

        return model;
    }

}
