package traceability;

import com.intellij.ui.treeStructure.Tree;
import mappings.MappingPopupMenu;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Gerrit Greiert on 07.11.16.
 */
public class TraceabilityTreeMouseListener implements MouseListener{

    private Tree tree;

    public TraceabilityTreeMouseListener(TraceabilityTree tree) {
        this.tree = tree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
            TreePath selectionPath = tree.getPathForLocation(e.getX(), e.getY());
            tree.setSelectionRow(row);
            TraceabilityPopupMenu menu = new TraceabilityPopupMenu(selectionPath);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
