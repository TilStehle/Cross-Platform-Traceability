package traceability.opener;

import com.intellij.openapi.ui.Messages;
import de.unihamburg.masterprojekt2016.traceability.PackagePointer;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityPointer;
import preferences.SettingsPropertyProvider;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by macbook on 04.01.17.
 */
public class DefaultTPointerOpener implements TPointerOpener {

    @Override
    public void openTraceabilityPointer(TraceabilityPointer pointer) {

        if (pointer instanceof PackagePointer){
            openTraceabilityPointer((PackagePointer) pointer);
        }
        else{
            File file = new File(pointer.getSourceFilePath());
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
