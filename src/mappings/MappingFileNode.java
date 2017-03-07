package mappings;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gerrit Greiert on 06.10.16.
 */
public class MappingFileNode extends DefaultMutableTreeNode {

    private File mappingFile;
    private MappingFileConnector connector;
    private DefaultTreeModel model;

    private DefaultMutableTreeNode typeNode;
    private DefaultMutableTreeNode methodNode;
    private DefaultMutableTreeNode modifierNode;

    private ArrayList<MappingNode> mappingNodes;

    public MappingFileNode(File file) throws FileNotFoundException {

        mappingFile = file;

        try {
            connector = new MappingFileConnector(mappingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mappingNodes = connector.getAllMappingsList();

        setUpChildren();

    }

    private void setUpChildren(){

        typeNode = new DefaultMutableTreeNode("Type mappingNodes");
        methodNode = new DefaultMutableTreeNode("Method mappingNodes");
        modifierNode = new DefaultMutableTreeNode("Modifier mappingNodes");

        add(typeNode);
        add(methodNode);
        add(modifierNode);

        updateChildren();
    }

    public void updateChildren(){

        typeNode.removeAllChildren();
        methodNode.removeAllChildren();
        modifierNode.removeAllChildren();

        for (MappingNode mappingNode : mappingNodes) {
            if(mappingNode.getMappingType() == MappingType.TYPE){
                typeNode.add(mappingNode);
            }
            if(mappingNode.getMappingType() == MappingType.METHOD){
                methodNode.add(mappingNode);
            }
            if(mappingNode.getMappingType() == MappingType.MODIFIER){
                modifierNode.add(mappingNode);
            }
        }

    }

    public void addMapping(MappingNode mappingNode) {
        mappingNodes.add(mappingNode);
        updateChildren();

        try {
            connector.addMappingToFile(mappingNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.reload(mappingNode.getParent());
    }

    public void editMapping(MappingNode initialMapping, MappingNode updatedMapping){

        DefaultMutableTreeNode previousParent = (DefaultMutableTreeNode) initialMapping.getParent();

        int index = mappingNodes.indexOf(initialMapping);
        mappingNodes.set(index, updatedMapping);
        updateChildren();

        try {
            connector.editMappingInFile(initialMapping, updatedMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.reload(previousParent);
        model.reload(updatedMapping.getParent());
    }

    public void deleteMapping(MappingNode mappingNode){

        DefaultMutableTreeNode previousParent = (DefaultMutableTreeNode) mappingNode.getParent();

        mappingNodes.remove(mappingNode);
        updateChildren();

        try {
            connector.deleteMappingFromFile(mappingNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.reload(previousParent);
    }

    public void registerParentTreeModel(DefaultTreeModel model){
        this.model = model;
    }

    @Override
    public String toString() {
        return mappingFile.getName();
    }
}
