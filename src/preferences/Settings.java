package preferences;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import services.J2SwiftService;

import javax.swing.*;

/**
 * Created by Gerrit Greiert on 12.07.16.
 */
public class Settings implements Configurable {

    private SettingsUI ui;
    private PropertiesComponent propertiesComponent;

    private String initialTargetPath;
    private String initialConfigFilePath;
    private String initialTargetProjectPath;

    public Settings() {
        ui = new SettingsUI();

        propertiesComponent = PropertiesComponent.getInstance();
        initialTargetPath = propertiesComponent.getValue(SettingsPropertyProvider.J2SWIFT_TARGET_PATH, "");
        initialConfigFilePath = propertiesComponent.getValue(SettingsPropertyProvider.J2SWIFT_CONFIG_FILE_PATH, "");
        initialTargetProjectPath = propertiesComponent.getValue(SettingsPropertyProvider.J2SWIFT_TARGET_PROJECT_PATH, "");
        ui.getTraceabilityModelPathTextField().setText(initialTargetPath);
        ui.getConfigFilePathTextField().setText(initialConfigFilePath);
        ui.getTargetProjectPathTextField().setText(initialTargetProjectPath);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "J2Swift preferences";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return ui.getPanel();
    }

    /**
     * Returns true if the user has modified any preferences
     * @return
     */
    @Override
    public boolean isModified() {

        return ui.getTraceabilityModelPathTextField().getText().equals(initialTargetPath) &&
                ui.getConfigFilePathTextField().getText().equals(initialConfigFilePath)&&
                ui.getTargetProjectPathTextField().getText().equals(initialTargetProjectPath)
                ? false : true;
    }

    /**
     * Applies the modified preferences and saves them in the PropertiesComponent
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        String newFilePath = ui.getTraceabilityModelPathTextField().getText();
        propertiesComponent.setValue(SettingsPropertyProvider.J2SWIFT_TARGET_PATH, newFilePath);
        initialTargetPath = newFilePath;

        String newConfigFilePath = ui.getConfigFilePathTextField().getText();
        propertiesComponent.setValue(SettingsPropertyProvider.J2SWIFT_CONFIG_FILE_PATH, newConfigFilePath);
        initialConfigFilePath = newConfigFilePath;

        String newTargetProjectPath = ui.getTargetProjectPathTextField().getText();
        propertiesComponent.setValue(SettingsPropertyProvider.J2SWIFT_TARGET_PROJECT_PATH, newTargetProjectPath);
        initialTargetProjectPath = newConfigFilePath;

        ServiceManager.getService(J2SwiftService.class).updateConfigFilePath(newConfigFilePath);

    }

    /**
     * Resets modified preferences to the initial state
     */
    @Override
    public void reset() {
        ui.getTraceabilityModelPathTextField().setText(initialTargetPath);
        ui.getConfigFilePathTextField().setText(initialConfigFilePath);
        ui.getTargetProjectPathTextField().setText(initialTargetProjectPath);
    }

    @Override
    public void disposeUIResources() {

    }
}
