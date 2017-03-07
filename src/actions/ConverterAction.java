package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import de.unihamburg.masterprojekt2016.converter.IConverter;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;
import services.J2SwiftService;

/**
 * Created by Gerrit Greiert on 11.07.16.
 */
public class ConverterAction extends AnAction{

    private String targetPath;

    /**
     * Executes the converter action as a background task after checking prerequisites
     * @param event
     */
    @Override
    public void actionPerformed(AnActionEvent event) {

        Project project = event.getProject();

        try {
            targetPath = SettingsPropertyProvider.getTargetPath();

            VirtualFile[] selectedElements = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);

            ConverterBackgroundTask task = new ConverterBackgroundTask(project, selectedElements, targetPath);
            ProgressManager.getInstance().run(task);
        }
        catch (MissingPropertyException e) {
            e.printStackTrace();
        }
    }

}
