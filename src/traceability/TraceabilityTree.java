package traceability;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.ui.treeStructure.Tree;
import de.unihamburg.masterprojekt2016.traceability.*;
import services.TraceabilityService;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.*;

/**
 * Created by Gerrit Greiert on 07.11.16.
 */
public class TraceabilityTree extends Tree implements Observer {

    private DefaultTreeModel treeModel;

    public TraceabilityTree() {
        super();

        TraceabilityService traceabilityService = ServiceManager.getService(TraceabilityService.class);

        traceabilityService.addObserver(this);

        TraceabilityModel model = traceabilityService.getTraceabilityModel();

        treeModel = makeTreeModelFromTraceabilityModel(model);

        this.setModel(treeModel);
        this.addMouseListener(new TraceabilityTreeMouseListener(this));
        this.setCellRenderer(new TraceabilityTreeCellRenderer());
        this.setRootVisible(false);

    }

    private DefaultTreeModel makeTreeModelFromTraceabilityModel(TraceabilityModel model) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Traceability Links");
        addSubModelToTreeModel(root, model);
        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        return treeModel;
    }

    private void addSubModelToTreeModel(DefaultMutableTreeNode node, TraceabilityModel model) {

        Map<TraceabilityPointer, TraceabilityModel> subModels = model.getSubModels();
        ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();

        for (Map.Entry<TraceabilityPointer, TraceabilityModel> entry : subModels.entrySet()) {

            DefaultMutableTreeNode subModelNode = new DefaultMutableTreeNode(entry.getValue());
            addSubModelToTreeModel(subModelNode, entry.getValue());
            nodes.add(subModelNode);

        }
        for (TraceabilityLinkSetWrapper linkSetWrapper : model.getTraceabilityLinksBySourcePointers().values()) {

            for (TraceabilityLink link : linkSetWrapper.get()) {
                nodes.add(new DefaultMutableTreeNode(link));
            }
        }

        Collections.sort(nodes, new NodeComparator());
        for (DefaultMutableTreeNode nodeToAdd : nodes){
            node.add(nodeToAdd);
        }
    }

    @Override
    public void update(Observable observable, Object arg) {

        TraceabilityService traceabilityService = ServiceManager.getService(TraceabilityService.class);
        TraceabilityModel model = traceabilityService.getTraceabilityModel();
        treeModel = makeTreeModelFromTraceabilityModel(model);
        this.setModel(treeModel);
    }

}