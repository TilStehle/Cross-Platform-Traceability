package toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by Gerrit Greiert on 30.08.16.
 */
public class ToolWindowFactoryImpl implements ToolWindowFactory {

    /**
     * Sets the content of the J2Swift tool window
     * @param project
     * @param toolWindow
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow){

        ToolWindowPanel toolWindowPanel = new ToolWindowPanel(project, toolWindow);
        toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(toolWindowPanel, "", false));

    }
}
