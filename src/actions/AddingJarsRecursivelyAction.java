package actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.J2SwiftConfigWriter;
import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.RequiredLibraryDO;
import de.unihamburg.masterprojekt2016.converter.j2swift.mapping.jarreader.util.ExtensionFilterVisitor;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by macbook on 15.12.16.
 */
public class AddingJarsRecursivelyAction extends AnAction{


    @Override
    public void actionPerformed(AnActionEvent event) {
        String configPath;
        try {
            configPath = SettingsPropertyProvider.getConfigPath();
            DataContext dataContext = event.getDataContext();

            VirtualFile virtualFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);

            if(virtualFile != null)
            {
                ExtensionFilterVisitor jarVisitor = new ExtensionFilterVisitor(".jar");
                try {
                    System.out.println(virtualFile.getCanonicalPath());
                    Files.walkFileTree(Paths.get(virtualFile.getCanonicalPath()), jarVisitor);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<Path> jarPathFiles = jarVisitor.getResults();
                //Convert Path to File
                List<File> files = jarPathFiles.stream().map(Path::toFile).collect(Collectors.toList());
                //Convert File to JarFile and add to List

                for(File jarFile : files)
                {

                    J2SwiftConfigWriter.addDataObject(new RequiredLibraryDO(jarFile.getAbsolutePath(), " "), configPath);
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
        DataContext dataContext = e.getDataContext();
        e.getPresentation().setEnabled(false);
        final Object virtualFile = dataContext.getData(DataKeys.LIBRARY);

        if(virtualFile != null )
        {
           System.out.println(virtualFile.toString());


            //TODO
            //System.out.println(virtualFile);
           // if (virtualFile.getPresentableName() != null)
           // {
            //    System.out.println(virtualFile.getPresentableName());
           // }

            if(true)
            {
                e.getPresentation().setEnabled(true);
            }
            else
                e.getPresentation().setEnabled(false);
        }
    }
}
