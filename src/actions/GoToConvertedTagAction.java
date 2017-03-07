package actions;


import java.util.*;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElementDecl;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityModel;
import de.unihamburg.masterprojekt2016.traceability.XmlElementDeclPointer;
import services.TraceabilityService;


public class GoToConvertedTagAction extends GotoActionBase{

    //private static Logger log = Logger.getInstance(GoToConvertedTagAction.class.getName());



    @Override
    protected void gotoActionPerformed(AnActionEvent anActionEvent) {

        DataContext dataContext = anActionEvent.getDataContext();
        PsiElement psiElement = dataContext.getData(CommonDataKeys.PSI_ELEMENT);
        VirtualFile virFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);

        if (psiElement != null && virFile != null && psiElement instanceof XmlElementDecl) {

            ASTNode node = ((XmlElementDecl) psiElement).getNameElement().getNode();
            System.out.println("____________________");
            System.out.println(virFile.getCanonicalPath() + " and " + node.getText());

            TraceabilityService service = ServiceManager.getService(TraceabilityService.class);
            TraceabilityModel traceabilityModel = service.getTraceabilityModel();
            XmlElementDeclPointer pointer = new XmlElementDeclPointer(virFile.getCanonicalPath(), node.getText());
            Set<TraceabilityLink> set = traceabilityModel.getTraceabilityLinkForPointer(pointer);
            System.out.println("Trace::::::::: " + set);
            //for (TraceabilityLink link : set) {
            //XCodeOpener.openXCodeAtLine("/Users/macbook/Desktop/OutpoutJ2SwiftPlugin/Base.lproj/Localizable.stringsdict",0);
            // XCodeOpener.openXCodeAtLine(link.getTarget().getSourceFilePath(),0);
            // break;

        }
    }

    @Override
    protected boolean hasContributors(DataContext dataContext) {
        System.out.println("hasContributors");
        return super.hasContributors(dataContext);
    }
    @Override
    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement psiElement = dataContext.getData(CommonDataKeys.PSI_ELEMENT);
        VirtualFile virtualFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        if(virtualFile != null && psiElement != null)
        {

            if(psiElement.getNode().getElementType().toString().equals("XML_ELEMENT_DECL"))
            {
                e.getPresentation().setEnabled(true);
            }
            else
            {
                e.getPresentation().setEnabled(false);
            }
        }
    }
}
