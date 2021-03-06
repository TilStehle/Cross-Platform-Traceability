package actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import iOS_Assets.ImageValidator;
import iOS_Assets.RassetsCreator;
import resourceConverter.ContextAdapterCreator;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by macbook on 24.11.16.
 */
public class ImageResourceConverterAction extends AnAction {

    private String targetPath;
    @Override
    public void actionPerformed(AnActionEvent event) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        targetPath = propertiesComponent.getValue("J2Swift_targetPath", "");

        if (targetPath.equals("")) { //if no target path is set show prompt
            int answer = Messages.showOkCancelDialog("Please configure a target path. Would you like to open the " +
                    "preferences?", "Missing target path", Messages.getQuestionIcon());
            if (answer == 0)
                ShowSettingsUtil.getInstance().showSettingsDialog(null, "J2Swift_settings");
            return;
        }
        VirtualFile images = event.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE);
        final ContextAdapterCreator _conAdapCreator = ContextAdapterCreator.getInstance();
        try {
            RassetsCreator creator = new RassetsCreator(targetPath,_conAdapCreator);
            if(images != null)
            {
                creator.copyAssetsToDestination(new File(images.getCanonicalPath()));
            }

        } catch (FileNotFoundException e) {
            Messages.showOkCancelDialog("Could not find target path","Missing target path",Messages.getErrorIcon());
        }
        VirtualFile selectedElement = event.getData(PlatformDataKeys.VIRTUAL_FILE);


    }

    @Override
    public void update(AnActionEvent e) {
        final VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        if(e.getPresentation() != null && virtualFile!=null)
        {
            if(ActionUtil.isDirectoryAndContainsImages(virtualFile.getCanonicalPath())|| virtualFile.getFileType().getName().equals("Images"))
            {
                e.getPresentation().setEnabled(true);
            }
            else {
                e.getPresentation().setEnabled(false);
            }
        }

    }
}
