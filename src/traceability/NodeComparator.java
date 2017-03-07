package traceability;

import de.unihamburg.masterprojekt2016.traceability.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Comparator;

public class NodeComparator implements Comparator<DefaultMutableTreeNode> {

    @Override
    public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {

        if (determineNodeRank(o1) == determineNodeRank(o2)) { //alphabetical within category

            if (o1.getUserObject() instanceof TraceabilityLink && o2.getUserObject() instanceof TraceabilityLink) {

                TraceabilityLink l1 = (TraceabilityLink) o1.getUserObject();
                TraceabilityLink l2 = (TraceabilityLink) o2.getUserObject();

                return l1.getSource().getDisplayName().compareToIgnoreCase(l2.getSource().getDisplayName());
            } else if (o1.getUserObject() instanceof TraceabilityModel && o2.getUserObject() instanceof TraceabilityModel) {

                TraceabilityModel m1 = (TraceabilityModel) o1.getUserObject();
                TraceabilityModel m2 = (TraceabilityModel) o2.getUserObject();

                return m1.description().compareToIgnoreCase(m2.description());
            } else {
                return -1;
            }
        } else { //different category
            return determineNodeRank(o1) - determineNodeRank(o2);
        }
    }

    /**
     * Returns rank depending on type of user object in the node (Higher number is lower in the tree)
     * @param node
     * @return
     */
    private int determineNodeRank(DefaultMutableTreeNode node) {

        Object userObject = node.getUserObject();

        if (userObject instanceof TraceabilityLink) {
            TraceabilityLink link = (TraceabilityLink) userObject;

            if (link.getSource() instanceof TypePointer)
                return 1;
            else if (link.getSource() instanceof AttributePointer)
                return 6;
            else if (link.getSource() instanceof ConstructorPointer)
                return 3;
            else if (link.getSource() instanceof MethodPointer)
                return 7;
            else
                return -1;
        } else if (userObject instanceof TraceabilityModel) {
            TraceabilityModel model = (TraceabilityModel) userObject;

            if (model.getDescribedArtefactPointer() instanceof TypePointer)
                return 9;
            else if (model.getDescribedArtefactPointer() instanceof ConstructorPointer)
                return 4;
            else if (model.getDescribedArtefactPointer() instanceof MethodPointer)
                return 8;
            else if (model.getDescribedArtefactPointer() instanceof PackagePointer)
                return 0;
            else if (model.getDescribedArtefactPointer() instanceof AttributePointer)
                return 5;
            else
                return -1;

        } else {
            return -1;
        }

    }

}
