package actions;

import actions.psiutils.PsiUtils;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import services.TraceabilityService;

/**
 * Created by Tilmann Stehle on 27.10.2016.
 */
public class GotoLinkedImplementationAction extends BaseCodeInsightAction {

    private TraceabilityService _traceabilityService;

    public GotoLinkedImplementationAction() {
        _traceabilityService = ServiceManager.getService(TraceabilityService.class);
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new GotoTraceabilityLinkHandler();
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAtOffset = file.findElementAt(offset);
        PsiElement psiElement=null;
        if (elementAtOffset != null) {
            psiElement = PsiUtils.getNearestTraceablePsiElement(elementAtOffset);
        }
        return psiElement!=null;
    }


}

