package actions.psiutils;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import de.unihamburg.masterprojekt2016.traceability.*;

/**
 * Created by Tilmann Stehle on 02.12.2016.
 */
public class TraceabilityPointerCreator {
    public static TraceabilityPointer getPointerForPsiElement(PsiElement psiElement)
    {
        TraceabilityPointer result= null;
        if(psiElement instanceof PsiMethod)
        {
            PsiMethod method = (PsiMethod) psiElement;
            if(method.isConstructor())
            {
                String className = method.getName();
                PsiClass containingClass = method.getContainingClass();
                String filePath = containingClass.getContainingFile().getVirtualFile().getCanonicalPath();
                TypePointer typePointer = new TypePointer(containingClass.getQualifiedName());
                TypePointerClassification typeClassification = getTypeClassifierForClass(containingClass);
                typePointer.setClassification(typeClassification);
                typePointer.setSourceFilePath(filePath);
                ConstructorPointer constructorPointer = new ConstructorPointer(className,typePointer,filePath);

                for (PsiParameter psiParameter : method.getParameterList().getParameters()) {
                    constructorPointer.addParameter(new Parameter(psiParameter.getName(), psiParameter.getType().getPresentableText()));
                }

                constructorPointer.setTypePointer(typePointer);
                result= constructorPointer;

            }
            else
            {
                String methodName = method.getName();
                MethodPointer methodPointer = new MethodPointer(methodName);
                methodPointer.setReturnType(method.getReturnType().getPresentableText());
                PsiClass containingClass = method.getContainingClass();
                for (PsiParameter psiParameter : method.getParameterList().getParameters()) {
                    methodPointer.addParameter(new Parameter(psiParameter.getName(), psiParameter.getType().getPresentableText()));
                }
                String filePath = containingClass.getContainingFile().getVirtualFile().getCanonicalPath();
                TypePointer typePointer = new TypePointer(containingClass.getQualifiedName());
                TypePointerClassification typeClassification = getTypeClassifierForClass(containingClass);
                typePointer.setClassification(typeClassification);
                typePointer.setSourceFilePath(filePath);
                methodPointer.setTypePointer(typePointer);
                result= methodPointer;
            }

        }
        else if (psiElement instanceof PsiClass)
        {
            PsiClass psiClass = (PsiClass) psiElement;
            String className = psiClass.getName();
            TypePointer classPointer = new TypePointer(psiClass.getQualifiedName());
            classPointer.setClassification(getTypeClassifierForClass(psiClass));
            String filePath= psiClass.getContainingFile().getVirtualFile().getCanonicalPath();
            classPointer.setSourceFilePath(filePath);
            result= classPointer;
        }
        else if (psiElement instanceof PsiField)
        {
            PsiClass containingClass =  ((PsiField) psiElement).getContainingClass();
            String className = containingClass.getName();
            TypePointer classPointer = new TypePointer(containingClass.getQualifiedName());
            classPointer.setClassification(getTypeClassifierForClass(containingClass));
            String filePath= containingClass.getContainingFile().getVirtualFile().getCanonicalPath();
            classPointer.setSourceFilePath(filePath);
            PsiField psiField= (PsiField) psiElement;
            String attributeType= psiField.getType().getPresentableText();
            result= new AttributePointer(psiField.getName(), classPointer,filePath,attributeType);

        }

        return result;

    }

    private static TypePointerClassification getTypeClassifierForClass(PsiClass psiClass) {
        if(psiClass.isEnum())
        {
            return TypePointerClassification.ENUM;
        }
        else if(psiClass instanceof PsiAnonymousClass)
        {
            return TypePointerClassification.ANONYMOUS_CLASS;
        }
        else if(psiClass.isInterface())
        {
            return TypePointerClassification.INTERFACE;
        }
        else if(psiClass.getContainingClass()!=null)
        {
            return TypePointerClassification.INNER_CLASS;
        }
        else
        {
            return TypePointerClassification.CLASS;
        }

    }
}
