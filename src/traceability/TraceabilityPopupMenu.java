package traceability;

import actions.XCodeController;
import com.intellij.openapi.ui.Messages;
import de.unihamburg.masterprojekt2016.traceability.*;
import preferences.IconProvider;
import preferences.SettingsPropertyProvider;
import traceability.opener.TPointerOpener;
import traceability.opener.TPointerOpenerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

/**
 * Created by Gerrit Greiert on 07.11.16.
 */
public class TraceabilityPopupMenu extends JPopupMenu {

    private TreePath path;

    public TraceabilityPopupMenu(TreePath path) {

        this.path = path;

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();

        if (selectedNode.getUserObject() instanceof TraceabilityLink) {

            TraceabilityLink selectedLink = (TraceabilityLink) selectedNode.getUserObject();

            JMenuItem openLinkItem = new JMenuItem("Open link", IconProvider.TRACEABILITY_ICON);
            openLinkItem.addActionListener(new OpenLinkListener(selectedLink));
            add(openLinkItem);
        }
        else if (selectedNode.getUserObject() instanceof TraceabilityModel){

            TraceabilityModel selectedModel = (TraceabilityModel) selectedNode.getUserObject();

            JMenuItem openLinkItem = new JMenuItem("Open described artefact", IconProvider.TRACEABILITY_ICON);
            openLinkItem.addActionListener(new OpenModelListener(selectedModel));
            add(openLinkItem);
        }
        //Add specified actions for different types of selected nodes
    }
}

class OpenLinkListener implements ActionListener {

    private TraceabilityLink link;

    public OpenLinkListener(TraceabilityLink link) {
        this.link = link;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        TPointerOpener tPointerOpener = TPointerOpenerFactory.createOpener();
        tPointerOpener.openTraceabilityPointer(link.getTarget());

    }
}

class OpenModelListener implements ActionListener {

    private TraceabilityModel model;

    public OpenModelListener(TraceabilityModel model){
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        TPointerOpener tPointerOpener = TPointerOpenerFactory.createOpener();
        TraceabilityPointer pointer = model.getDescribedArtefactPointer();

        if (pointer instanceof PackagePointer){
            tPointerOpener.openTraceabilityPointer(pointer);
        }
        else{
            Set<TraceabilityLink> links = model.getTraceabilityLinkForPointer(pointer);
            if (links.size() == 1) {
                pointer = links.iterator().next().getTarget(); //TODO: Enable selection instead of taking first
                tPointerOpener.openTraceabilityPointer(pointer);
            }else{
                System.out.println("Links in Wrapper: " + links.size());
            }

            //tPointerOpener.openTraceabilityPointer(pointer);
        }

        //tPointerOpener.openTraceabilityPointer(pointer);

    }
}