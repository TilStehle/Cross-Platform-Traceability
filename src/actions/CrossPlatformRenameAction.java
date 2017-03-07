package actions;

import actions.psiutils.PsiUtils;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.actions.BaseRefactoringAction;
import com.intellij.refactoring.rename.AutomaticRenamingDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Tilmann Stehle on 06.12.2016.
 */
public class CrossPlatformRenameAction extends BaseRefactoringAction{
    @Override
    protected boolean isAvailableInEditorOnly() {
        return false;
    }

    @Override
    protected boolean isEnabledOnElements(@NotNull PsiElement[] psiElements) {
return true;
    }
    @Override
    protected boolean isAvailableOnElementInEditorAndFile(@NotNull PsiElement element, @NotNull Editor editor, @NotNull PsiFile file, @NotNull DataContext context) {

        PsiMethod method=null;
        if (element != null) {
            method = PsiUtils.iterateToParentOfType(PsiMethod.class, element);
        }
        return method!=null;
    }

        @Nullable
    @Override
    protected RefactoringActionHandler getHandler(@NotNull DataContext dataContext) {
        return new CrossPlatformRenameHandler();
    }
}
