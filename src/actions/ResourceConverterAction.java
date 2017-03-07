package actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;
import resourceConverter.IOSResCreator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Gerrit Greiert on 02.10.16.
 */
public class ResourceConverterAction extends AnAction {

    private String targetPath;

    /**
     * Executes the conversion of string resources action after checking prerequisites
     * @param event
     */
    @Override
    public void actionPerformed(AnActionEvent event) {

        VirtualFile selectedElement = event.getData(PlatformDataKeys.VIRTUAL_FILE);

        try {
            IOSResCreator iosResCreator = new IOSResCreator(new File(SettingsPropertyProvider.getTargetPath()));
            iosResCreator.createResources(new File(selectedElement.getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        final VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        if(file!=null)
        {
            if(file.isDirectory() == false && file.getExtension().equals("java")){
                e.getPresentation().setEnabled(false);
            }
            else {
                e.getPresentation().setEnabled(true);
            }
        }

    }
}
