package toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBTabbedPane;

/**
 * Created by Gerrit Greiert on 05.10.16.
 */
public class ToolWindowPanel extends JBTabbedPane {

    private Project project;
    private ToolWindow toolWindow;

    public ToolWindowPanel(Project project, ToolWindow toolWindow) {

        this.project = project;
        this.toolWindow = toolWindow;
        initGui();
    }

    private void initGui(){
        addTab("Traceability Links", new TraceabilityLinksPanel());
        addTab("Mappings", new MappingsPanel());
    }

}
