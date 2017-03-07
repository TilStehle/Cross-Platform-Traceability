package actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import de.unihamburg.masterprojekt2016.converter.IConverter;
import de.unihamburg.masterprojekt2016.converter.j2swift.J2Swift;
import org.jetbrains.annotations.NotNull;
import preferences.MissingPropertyException;
import services.J2SwiftService;
import services.TraceabilityService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gerrit Greiert on 28.09.16.
 */
public class ConverterBackgroundTask extends Task.Backgroundable {

    private String projectPath;
    private String targetPath;

    private VirtualFile[] selectedElements;
    private ArrayList<VirtualFile> filesToConvert;

    public ConverterBackgroundTask(Project project, VirtualFile[] selectedElements, String targetPath) {
        super(project, "J2Swift");

        projectPath = project.getBasePath();

        this.targetPath = targetPath;
        this.selectedElements = selectedElements;

    }

    public ConverterBackgroundTask(Project project, PsiFile[] selectedElements, String targetPath) {
        super(project, "J2Swift");

        projectPath = project.getBasePath();

        this.targetPath = targetPath;
        this.selectedElements = convertPsiFilesToVirtualFiles(selectedElements);

    }

    private IConverter getConverter(){
        try {
            J2SwiftService j2SwiftService = ServiceManager.getService(J2SwiftService.class);
            IConverter converter = j2SwiftService.getConverter();
            return converter;
        }
        catch (MissingPropertyException ex){
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Runs the conversion task in the background and updates the progress indicator
     * @param progressIndicator
     */
    @Override
    public void run(@NotNull ProgressIndicator progressIndicator) {

        filesToConvert = new ArrayList<VirtualFile>();

        processSelectedElements(selectedElements);

        double progressFraction = 1d / (double) filesToConvert.size();

        for (VirtualFile file: filesToConvert) {
            try {
                convertJavaFileToSwift(file);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(file.getName() + " failed to convert");
            }
            progressIndicator.setFraction(progressIndicator.getFraction() + progressFraction);
        }

    }

    private void processSelectedElements(VirtualFile[] selectedElements) {

        for (VirtualFile file : selectedElements) {

            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                processSelectedElements(file.getChildren());

            } else {
                //System.out.println("File: " + file.getName());
                if (file.getExtension().equals("java")) {
                    filesToConvert.add(file);
                }
            }

        }
    }

    private void convertJavaFileToSwift(VirtualFile file) throws IOException {

        String convertedContent = null;
        String targetPathForFile = constructTargetPathForFile(file);

        IConverter converter = getConverter();

        convertedContent = converter.convert(file.getInputStream(), file.getCanonicalPath(), targetPathForFile);

        TraceabilityService traceabilityService = ServiceManager.getService(TraceabilityService.class);
        traceabilityService.setTraceabilityModel(converter.getTraceabilityModel());

        File convertedFile = new File(targetPathForFile);
        convertedFile.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(convertedFile, false)) {
            fw.write(convertedContent);
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private String constructTargetPathForFile(VirtualFile file){

        String specifiedPath = file.getPath().replaceFirst(projectPath, "");
        specifiedPath = specifiedPath.replaceFirst("\\.java", ".swift");
        specifiedPath = targetPath + specifiedPath;
        return specifiedPath;
    }

    private VirtualFile[] convertPsiFilesToVirtualFiles(PsiFile[] selectedElements) {

        VirtualFile[] virtualFiles = new VirtualFile[selectedElements.length];

        for (int index = 0; index < virtualFiles.length; index++) {
            virtualFiles[index] = selectedElements[index].getVirtualFile();
        }

        return virtualFiles;
    }
}
