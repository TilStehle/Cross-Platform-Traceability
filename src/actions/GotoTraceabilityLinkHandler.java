package actions;

import actions.psiutils.PsiUtils;
import actions.psiutils.TraceabilityPointerCreator;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.ide.IconProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.*;
import com.intellij.ui.components.JBList;
import de.unihamburg.masterprojekt2016.traceability.*;
import org.jetbrains.annotations.NotNull;
import services.TraceabilityService;
import traceability.opener.TPointerOpenerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tilmann Stehle on 01.12.2016.
 */
public class GotoTraceabilityLinkHandler implements CodeInsightActionHandler {
    private final TraceabilityService _traceabilityService;

    public GotoTraceabilityLinkHandler() {
        _traceabilityService = ServiceManager.getService(TraceabilityService.class);
    }


    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAtOffset = psiFile.findElementAt(offset);
        if (elementAtOffset == null) {
            return;
        }
        final PsiElement nearestTraceablePsiElement = PsiUtils.getNearestTraceablePsiElement(elementAtOffset);
        if (nearestTraceablePsiElement == null) {
            return;
        }
        ArrayList<TraceabilityLink> linksForPsiElementAtCursor = new ArrayList<TraceabilityLink>(getLinksStartingAtPsiElement(nearestTraceablePsiElement));
        JBPopupFactory popupFactory = ServiceManager.getService(JBPopupFactory.class);

        JList<TraceabilityLink> list = new JBList(linksForPsiElementAtCursor);
        list.setCellRenderer(new ListCellRenderer<TraceabilityLink>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends TraceabilityLink> list, TraceabilityLink value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.getTarget().getDisplayName());
                label.setIcon(getIconForPointer(value.getTarget()));
                return label;
            }

            private Icon getIconForPointer(TraceabilityPointer pointer) {
                if(pointer instanceof MethodPointer)
                {
                    return preferences.IconProvider.LINK_METHOD;
                }
                else if(pointer instanceof AttributePointer)
                {
                    return preferences.IconProvider.LINK_FIELD;
                }
                else if(pointer instanceof ConstructorPointer)
                {
                    return preferences.IconProvider.LINK_CONSTRUCTOR;
                }
                else if(pointer instanceof TypePointer)
                {
                    TypePointer typePointer =(TypePointer) pointer;
                if(typePointer.getClassification() == TypePointerClassification.INTERFACE)
                {
                    return preferences.IconProvider.LINK_INTERFACE;
                }
                else
                    return preferences.IconProvider.LINK_CLASS;
                }
                else return preferences.IconProvider.TRACEABILITY_ICON;
            }
        });
        PopupChooserBuilder listPopupBuilder = popupFactory.createListPopupBuilder(list);
        JBPopup popup = listPopupBuilder.createPopup();
        popup.addListener(new JBPopupListener.Adapter() {
            @Override
            public void onClosed(LightweightWindowEvent event) {
                int selectedIndex = list.getSelectedIndex();
                if (event.isOk()) {
                    TraceabilityPointer target = linksForPsiElementAtCursor.get(selectedIndex).getTarget();
                    TPointerOpenerFactory.createOpener().openTraceabilityPointer(target);
                }
            }
        });
        popup.showInBestPositionFor(editor);

    }

    private Set<TraceabilityLink> getLinksStartingAtPsiElement(PsiElement psiElement) {
        TraceabilityPointer sourcePointer = TraceabilityPointerCreator.getPointerForPsiElement(psiElement);
        Set<TraceabilityLink> links = _traceabilityService.getTraceabilityModel().getTraceabilityLinkForPointer(sourcePointer);
        if (links == null) {
            links = new HashSet<>();
        }
        return links;
    }


    @Override
    public boolean startInWriteAction() {
        return false;
    }



    private class popUpBuilder extends BaseListPopupStep {

    }
}
