package actions;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packageDependencies.DependencyValidationManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.scope.packageSet.CustomScopesProvider;
import com.intellij.psi.search.scope.packageSet.NamedScope;
import com.intellij.psi.search.scope.packageSet.NamedScopeManager;
import com.intellij.psi.search.scope.packageSet.NamedScopesHolder;
import com.intellij.util.indexing.FileBasedIndex;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.intellij.psi.search.scope.packageSet.CustomScopesProvider.CUSTOM_SCOPES_PROVIDER;

/**
 * Created by Gerrit Greiert on 16.11.16.
 */
public class ScopeConverterAction extends AnAction {

    private Set<PsiFile> psiFiles;
    private Set<PsiFile> filesToConvert;

    private String targetPath;
    private Project project;

    @Override
    public void actionPerformed(AnActionEvent event) {

        project = event.getProject();

        try {
            targetPath = SettingsPropertyProvider.getTargetPath();

            //Get all java files in the current project and save as PsiFile
            Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
            psiFiles = new HashSet<PsiFile>();
            for (VirtualFile vf : files) {
                psiFiles.add(PsiManager.getInstance(project).findFile(vf));
            }

            //Show ScopeSelectionDialog and dispatch
            ScopeSelectionDialog scopeSelectionDialog = new ScopeSelectionDialog(project, getScopes());
            scopeSelectionDialog.show();
            if (scopeSelectionDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
                convertSelectedScopes(scopeSelectionDialog.getSelectedScopes());
            }
        }
        catch (MissingPropertyException ex){
            ex.printStackTrace();
        }
    }

    private Set<NamedScope> getScopes(){

        Set<NamedScope> allScopesSet = new HashSet<NamedScope>();

        //Get default scopes
        DependencyValidationManager dependencyValidationManager = DependencyValidationManager.getInstance(project);
        NamedScope[] defaultScopesArray = dependencyValidationManager.getScopes();
        allScopesSet.addAll(Arrays.asList(defaultScopesArray));

        //Get local scopes
        NamedScopeManager namedScopeManager = NamedScopeManager.getInstance(project);
        for (NamedScope scope: namedScopeManager.getScopes()){
            allScopesSet.add(scope);
        }

        //Get custom (shared) scopes
        CustomScopesProvider[] customScopesProviders = CUSTOM_SCOPES_PROVIDER.getExtensions(project);
        for (CustomScopesProvider scopesProvider : customScopesProviders) {
            allScopesSet.addAll(scopesProvider.getFilteredScopes());
        }

        return allScopesSet;
    }

    private void convertSelectedScopes(Set<NamedScope> selectedScopes){

        filesToConvert = new HashSet<PsiFile>();
        for (PsiFile psiFile : psiFiles) {
            for (NamedScope scope : selectedScopes) {
                if (scope.getValue().contains(psiFile, NamedScopesHolder.getHolder(project, scope.getName(), null))) {
                    filesToConvert.add(psiFile);
                }
            }
        }

        PsiFile[] selectedElements = filesToConvert.toArray(new PsiFile[filesToConvert.size()]);

        ConverterBackgroundTask task = new ConverterBackgroundTask(project, selectedElements, targetPath);
        ProgressManager.getInstance().run(task);
    }
}
