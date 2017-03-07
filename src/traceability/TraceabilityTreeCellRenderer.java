package traceability;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;
import de.unihamburg.masterprojekt2016.traceability.*;
import preferences.IconProvider;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Created by Gerrit Greiert on 24.11.16.
 */
public class TraceabilityTreeCellRenderer implements TreeCellRenderer {

    /**
     * Gets formatted cell label depending on the type of element in the Tree
     * @param tree
     * @param value
     * @param selected
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     * @return
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        JPanel cellPanel = new JPanel(new GridLayout(1,2,3,0));

        JLabel sourceLabel = new JLabel("Traceability Model");
        JLabel targetLabel = new JLabel("");
        sourceLabel.setForeground(getMainColor(selected));
        targetLabel.setForeground(getSecondaryColor(selected));


        cellPanel.add(sourceLabel);
        cellPanel.add(targetLabel);

        if (node.getUserObject() instanceof TraceabilityLink) {
            TraceabilityLink link = (TraceabilityLink) node.getUserObject();

            sourceLabel.setText(link.toString()); //Default
            cellPanel.setToolTipText(link.toString()); //Set tooltip

            if (link.getSource() instanceof TypePointer) {
                TypePointer pointer = (TypePointer) link.getSource();

                if (pointer.getClassification().isClass())
                    sourceLabel.setIcon(IconProvider.LINK_CLASS);
                if (pointer.getClassification().isInterface())
                    sourceLabel.setIcon(IconProvider.LINK_INTERFACE);
                if (pointer.getClassification().isEnum())
                    sourceLabel.setIcon(IconProvider.LINK_ENUM);

                sourceLabel.setText(pointer.getDisplayName());
                targetLabel.setText("("+ link.getTarget().getDisplayName() +")");
            }
            else if (link.getSource() instanceof MethodPointer) {
                MethodPointer pointer = (MethodPointer) link.getSource();

                sourceLabel.setIcon(IconProvider.LINK_METHOD);
                sourceLabel.setText(pointer.getDisplayName());
                targetLabel.setText("("+ link.getTarget().getDisplayName() +")");
            }
            else if (link.getSource() instanceof ConstructorPointer) {
                ConstructorPointer pointer = (ConstructorPointer) link.getSource();

                sourceLabel.setIcon(IconProvider.LINK_CONSTRUCTOR);
                sourceLabel.setText(pointer.getDisplayName());
                targetLabel.setText("("+ link.getTarget().getDisplayName() +")");
            }
            else if (link.getSource() instanceof AttributePointer) {
                AttributePointer pointer = (AttributePointer) link.getSource();

                sourceLabel.setIcon(IconProvider.LINK_FIELD);
                sourceLabel.setText(pointer.getDisplayName());
                targetLabel.setText("("+ link.getTarget().getDisplayName() +")");
            }

        } else if (node.getUserObject() instanceof TraceabilityModel) {
            TraceabilityModel model = (TraceabilityModel) node.getUserObject();

            sourceLabel.setIcon(IconProvider.TRACEABILITY_ICON);
            sourceLabel.setText(model.getDescribedArtefactPointer().toString()); //Default
            cellPanel.setToolTipText(model.toString()); //set tooltip

            if (model.getDescribedArtefactPointer() instanceof TypePointer) { //Class
                TypePointer typePointer = (TypePointer) model.getDescribedArtefactPointer();

                if (typePointer.getClassification().isClass())
                    sourceLabel.setText("Class: " + typePointer.getDisplayName());
                if (typePointer.getClassification().isInterface())
                    sourceLabel.setText("Interface: " + typePointer.getDisplayName());
                if (typePointer.getClassification().isEnum())
                    sourceLabel.setText("Enum: " + typePointer.getDisplayName());

                targetLabel.setText("(" + typePointer.getFullyQualifiedName() + ")");
            }
            else if (model.getDescribedArtefactPointer() instanceof MethodPointer) { //Method
                MethodPointer methodPointer = (MethodPointer) model.getDescribedArtefactPointer();

                sourceLabel.setIcon(IconProvider.LINK_METHOD);
                sourceLabel.setText("Method: " + methodPointer.getDisplayName());
                if (node.getChildCount() > 1)
                    targetLabel.setText("("+ node.getChildCount() + ")");
            }
            else if (model.getDescribedArtefactPointer() instanceof ConstructorPointer) { //Constructor
                ConstructorPointer constructorPointer = (ConstructorPointer) model.getDescribedArtefactPointer();

                sourceLabel.setIcon(IconProvider.LINK_CONSTRUCTOR);
                sourceLabel.setText("Constructor: " + constructorPointer.getDisplayName());
            }
            else if (model.getDescribedArtefactPointer() instanceof PackagePointer) { //Package
                PackagePointer packagePointer = (PackagePointer) model.getDescribedArtefactPointer();

                sourceLabel.setIcon(IconProvider.LINK_PACKAGE);
                sourceLabel.setText("Package: " + packagePointer.getDisplayName());
            }
            else if (model.getDescribedArtefactPointer() instanceof AttributePointer) { //Attribute
                AttributePointer attributePointer = (AttributePointer) model.getDescribedArtefactPointer();

                sourceLabel.setIcon(IconProvider.LINK_FIELD);
                sourceLabel.setText("Field: " + attributePointer.getDisplayName());
            }

        }

        return cellPanel;
    }

    private JBColor getMainColor(boolean selected){
        return selected ? JBColor.WHITE : JBColor.BLACK;
    }
    private JBColor getSecondaryColor(boolean selected){
        return selected ? JBColor.LIGHT_GRAY : JBColor.GRAY;
    }
}
