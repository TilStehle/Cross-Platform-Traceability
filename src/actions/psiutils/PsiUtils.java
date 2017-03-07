package actions.psiutils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

/**
 * Created by Tilmann Stehle on 11.11.2016.
 */
public class PsiUtils {
    public static <T extends PsiElement> T iterateToParentOfType(Class<T> clazz, PsiElement descendent)
    {
        if (descendent == null)
        {
            return null;
        } else
        {
            if (descendent != null && clazz.isAssignableFrom(descendent.getClass()))
            {
                return (T) descendent;
            } else
            {
                return iterateToParentOfType(clazz, descendent.getParent());
            }

        }


    }
    public static PsiElement getNearestTraceablePsiElement(PsiElement elementAtCaret) {
        PsiElement nearestTraceablePsiElement=null;
        nearestTraceablePsiElement= PsiUtils.iterateToParentOfType(PsiMethod.class, elementAtCaret);
        if(nearestTraceablePsiElement==null)
        {
            nearestTraceablePsiElement= PsiUtils.iterateToParentOfType(PsiField.class, elementAtCaret);
        }
        if(nearestTraceablePsiElement==null)
        {
            nearestTraceablePsiElement= PsiUtils.iterateToParentOfType(PsiClass.class, elementAtCaret);
        }
        return nearestTraceablePsiElement;
    }
}
