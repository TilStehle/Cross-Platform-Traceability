package mappings;

import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Gerrit Greiert on 17.10.16.
 */
public class MappingTreeMouseListener implements MouseListener {

    private Tree tree;

    public MappingTreeMouseListener(Tree tree){
        this.tree = tree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if(SwingUtilities.isRightMouseButton(e)){
            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
            TreePath selectionPath = tree.getPathForLocation(e.getX(), e.getY());
            tree.setSelectionRow(row);
            MappingPopupMenu menu = new MappingPopupMenu(selectionPath);
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
