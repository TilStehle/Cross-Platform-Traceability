package actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.ui.Messages;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Gerrit Greiert on 23.08.16.
 */
public class TargetPathAction extends AnAction {

    /**
     * Opens the configured target path if configured
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {


        try {
            Desktop.getDesktop().open(new File(SettingsPropertyProvider.getTargetPath()));
        } catch (IOException ex) {
            Messages.showMessageDialog("Target path couldn't be opened", "Error", Messages.getErrorIcon());
            ex.printStackTrace();
        } catch (MissingPropertyException ex) {
            ex.printStackTrace();
        }
    }
}
