package traceability.opener;

import actions.XCodeController;
import com.intellij.openapi.ui.Messages;
import de.unihamburg.masterprojekt2016.traceability.*;
import preferences.SettingsPropertyProvider;

import java.awt.*;
import java.io.File;

/**
 * Created by Gerrit Greiert on 04.01.17.
 */
public class MacTPointerOpener implements TPointerOpener {

    @Override
    public void openTraceabilityPointer(TraceabilityPointer pointer) {

        if (pointer instanceof TypePointer){
            openTraceabilityPointer((TypePointer) pointer);
        }
        else if (pointer instanceof MethodPointer){
            openTraceabilityPointer((MethodPointer) pointer);
        }
        else if (pointer instanceof ConstructorPointer){
            openTraceabilityPointer((ConstructorPointer) pointer);
        }
        else if (pointer instanceof AttributePointer){
            openTraceabilityPointer((AttributePointer) pointer);
        }
        else if (pointer instanceof PackagePointer){
            openTraceabilityPointer((PackagePointer) pointer);
        }
        else{
            Messages.showInfoMessage("The selected type of Traceability Pointer cannot be opened by " + this.getClass().getName(), "J2Swift");
        }

    }

    public void openTraceabilityPointer(TypePointer pointer) {
        XCodeController.openXCodeAtLine(pointer.getSourceFilePath(), 0);
    }

    public void openTraceabilityPointer(MethodPointer pointer) {
        XCodeController.openXCodeAtMethod(pointer);
    }

    public void openTraceabilityPointer(ConstructorPointer pointer) {
        XCodeController.openXCodeAtConstructor(pointer);
    }

    public void openTraceabilityPointer(AttributePointer pointer) {
        XCodeController.openXCodeAtAttribute(pointer);
    }

    public void openTraceabilityPointer(PackagePointer pointer) {
        try {
            String sourceString = File.separator + "src" + File.separator;
            String path = SettingsPropertyProvider.getTargetPath() + sourceString + pointer.getName();
            Desktop.getDesktop().open(new File(path));
        } catch (Exception ex) {
            Messages.showErrorDialog("Target package path couldn't be opened", "J2Swift");
            ex.printStackTrace();
        }
    }
}
