package actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.J2SwiftConfigWriter;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.RequiredLibraryDO;
import mappings.MappingsReader;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 */
public class AddingJarAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
        String configPath;
        try {
            configPath = SettingsPropertyProvider.getConfigPath();
            DataContext dataContext = event.getDataContext();
            VirtualFile[] virtualFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
            if(virtualFile != null)
            {
                for(VirtualFile file : virtualFile)
                {
                    if(file.getPresentableName().endsWith(".jar"))
                    {
                        System.out.println("Path: " + file.getPath());
                        System.out.println("CPath: " + file.getCanonicalPath());
                        System.out.println("Name:  " + file.getName());
                        J2SwiftConfigWriter.addDataObject(new RequiredLibraryDO(file.getCanonicalPath().replaceAll("!/$", "")," "),configPath);
                    }

                }

            }
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        final VirtualFile[] virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        boolean show = false;
        e.getPresentation().setEnabled(show);
        if(virtualFile != null)
        {
            for(VirtualFile file : virtualFile) {
                System.out.println(file.getFileType().getName());
                show = file.getFileType().equals(FileTypes.ARCHIVE);
            }
            e.getPresentation().setEnabled(show);

        }
    }
}
