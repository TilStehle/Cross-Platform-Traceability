package preferences.ExtensionUI;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.search.scope.packageSet.NamedScope;
import org.jetbrains.annotations.Nullable;
import preferences.ExtensionUI.XmlEditor.XmlViewer;

import javax.swing.*;
import java.io.File;
import java.util.Set;

/**
 * Created by Nils-Hendrik Berger on 12.01.17.
 */
public class XmlViewerDialog extends DialogWrapper {
    private File currentFile;

    protected XmlViewerDialog(@Nullable Project project, File currentFile){
        super(project);
        this.currentFile = currentFile;
        setResizable(true);
        setVerticalStretch(1.8f);
        setHorizontalStretch(1.5f);
        pack();
        init();
    }
    protected XmlViewerDialog(@Nullable Project project){
        super(project);
        setResizable(true);
        setSize(1200,800);
        pack();
        init();
    }



    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        if(currentFile != null)
        {
            XmlViewer viewer = new XmlViewer(currentFile);
            setTitle("XML Viewer" + "[" + viewer.getDocumentName() + "]");
            return viewer;
        }
        else
        {
            XmlViewer viewer = new XmlViewer(currentFile);
            setTitle("XML Viewer" + "[" + viewer.getDocumentName() + "]");
            return viewer;
        }

    }
}
