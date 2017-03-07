package preferences;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.ui.Messages;

/**
 * Created by Gerrit Greiert on 05.12.16.
 */
public class SettingsPropertyProvider {

    public static final String J2SWIFT_TARGET_PATH = "J2Swift_targetPath";
    public static final String J2SWIFT_CONFIG_FILE_PATH = "J2Swift_configFilePath";
    public static final String J2SWIFT_TARGET_PROJECT_PATH = "J2Swift_targetProjectPath";
    public static final String J2SWIFT_TRACEABILITY_FILE_PATH = "J2Swift_traceabilityFilePath";

    /**
     * Tries to return the target path string and shows prompt if it isn't configured
     * @return
     * @throws MissingPropertyException
     */
    public static String getTargetPath() throws MissingPropertyException {

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String targetPath = propertiesComponent.getValue(J2SWIFT_TARGET_PATH, "");

        if (targetPath.equals("")) { //if no target path is set show prompt
            int answer = Messages.showOkCancelDialog("Please configure a target path. Would you like to open the " +
                    "preferences?", "Missing target path", Messages.getQuestionIcon());
            if (answer == 0)
                ShowSettingsUtil.getInstance().showSettingsDialog(null, Settings.class);
            throw new MissingPropertyException("No target path configured in the preferences");
        }
        return targetPath;
    }

    /**
     * Tries to return the config path string and shows prompt if it isn't configured
     * @return
     * @throws MissingPropertyException
     */
    public static String getConfigPath() throws MissingPropertyException{

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String configPath = propertiesComponent.getValue(J2SWIFT_CONFIG_FILE_PATH, "");

        if (configPath.equals("")) { //if no config path is set show prompt
            int answer = Messages.showOkCancelDialog("Please configure a config path. Would you like to open the " +
                    "preferences?", "Missing config path", Messages.getQuestionIcon());
            if (answer == 0)
                ShowSettingsUtil.getInstance().showSettingsDialog(null, Settings.class);
            throw new MissingPropertyException("No config path configured in the preferences");
        }
        return configPath;
    }

    /**
     * Tries to return the traceability file path string and shows prompt if it isn't configured
     * @return
     * @throws MissingPropertyException
     */
    public static String getTraceabilityFilePath() throws MissingPropertyException{

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String configPath = propertiesComponent.getValue(J2SWIFT_TRACEABILITY_FILE_PATH, getTargetPath());

        if (configPath.equals("")) { //if no config path is set show prompt
            int answer = Messages.showOkCancelDialog("Please configure a traceability path. Would you like to open the " +
                    "preferences?", "Missing traceability path", Messages.getQuestionIcon());
            if (answer == 0)
                ShowSettingsUtil.getInstance().showSettingsDialog(null, Settings.class);
            throw new MissingPropertyException("No config path configured in the preferences");
        }
        return configPath;
    }

    /**
     * Tries to return the target XCode project file path string
     * @return
     */
    public static String getTargetProjectFilePath(){

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String targetProjectPath = propertiesComponent.getValue(J2SWIFT_TARGET_PROJECT_PATH, "");

        return targetProjectPath;
    }
}
