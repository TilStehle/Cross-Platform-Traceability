package toolwindow;

import traceability.TraceabilityTree;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

/**
 * Created by Gerrit Greiert on 05.10.16.
 */
public class TraceabilityLinksPanel extends JScrollPane {

    public TraceabilityLinksPanel() {

        TraceabilityTree tree = new TraceabilityTree();
        add(tree);
        setViewportView(tree);

    }
}
