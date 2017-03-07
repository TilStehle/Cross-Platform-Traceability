package preferences;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.ex.FileTextFieldImpl;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Gerrit Greiert on 12.07.16.
 */
public class SettingsUI {

    private JPanel mainPanel;
    private TextFieldWithBrowseButton traceabilityModelPathTextField;
    private TextFieldWithBrowseButton configFilePathField;
    private TextFieldWithBrowseButton targetProjectPathTextField;


    /**
     * Creates the Swing UI used in the preferences for the plugin
     */
    public SettingsUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalFlowLayout(true, false));
        mainPanel.add(createTargetPathPanel());
        mainPanel.add(createConfigFilePathPanel());
        mainPanel.add(createTargetProjectPathPanel());
        mainPanel.add(createGenerateDefaultConfigPanel());
    }

    private JPanel createTargetProjectPathPanel() {
        JPanel targetProjectPathPanel = new JPanel();
        targetProjectPathPanel.setLayout(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel("Target XCode Project File:");
        targetProjectPathTextField = new TextFieldWithBrowseButton();
        targetProjectPathTextField.setEditable(false);

        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        targetProjectPathTextField.addBrowseFolderListener("J2Swift", "Select the target project used for navigating along traceability links",
                null, fileChooserDescriptor);

        targetProjectPathPanel.add(titleLabel, BorderLayout.LINE_START);
        targetProjectPathPanel.add(targetProjectPathTextField, BorderLayout.CENTER);

        return targetProjectPathPanel;
    }

    /**
     * Returns the JPanel containing the preferences UI
     * @return
     */
    public JPanel getPanel() {
        return mainPanel;
    }

    public TextFieldWithBrowseButton getTraceabilityModelPathTextField() {
        return traceabilityModelPathTextField;
    }

    private JPanel createTargetPathPanel() {
        JPanel targetPathPanel = new JPanel();
        targetPathPanel.setLayout(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel("Output folder:");
        traceabilityModelPathTextField = new TextFieldWithBrowseButton();
        traceabilityModelPathTextField.setEditable(false);

        traceabilityModelPathTextField.addBrowseFolderListener("J2Swift", "Select a J2Swift target path for the converted files",
                null, new FileChooserDescriptor(false, true, false, false, false, false));

        targetPathPanel.add(titleLabel, BorderLayout.LINE_START);
        targetPathPanel.add(traceabilityModelPathTextField, BorderLayout.CENTER);

        return targetPathPanel;
    }

    public TextFieldWithBrowseButton getConfigFilePathTextField() {
        return configFilePathField;
    }

    private JPanel createConfigFilePathPanel(){
        JPanel configFilePathPanel = new JPanel();
        configFilePathPanel.setLayout(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel("Configuration file path:");
        configFilePathField = new TextFieldWithBrowseButton();
        configFilePathField.setEditable(false);

        configFilePathField.addBrowseFolderListener("J2Swift", "Select the J2Swift configuration file that should be used",
                null, new FileChooserDescriptor(true, false, false, false, false, false));

        configFilePathPanel.add(titleLabel, BorderLayout.LINE_START);
        configFilePathPanel.add(configFilePathField, BorderLayout.CENTER);

        return configFilePathPanel;
    }

    private JPanel createGenerateDefaultConfigPanel(){
        JPanel generateDefaultConfigPanel = new JPanel();
        generateDefaultConfigPanel.setLayout(new BorderLayout(5,5));

        JLabel infoLabel = new JLabel("If you don't have the J2Swift config files, click here to generate defaults:");
        JButton generateButton = new JButton("Generate");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
                descriptor.setDescription("Select directory in which to generate the default config files");
                VirtualFile selectedDirectory = FileChooser.chooseFile(descriptor, null, null);

                if (selectedDirectory != null) {

                    try {
                        String configDirectoryString = selectedDirectory.getPath() + File.separator + "J2Swift_config";
                        DefaultConfigGenerator generator = new DefaultConfigGenerator();
                        String configPath = generator.generateDefaultConfigFiles(configDirectoryString);
                        configFilePathField.setText(configPath);
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                        Messages.showErrorDialog("Generating default config files failed - Please try again", "J2Swift");
                    }
                }
            }
        });

        generateDefaultConfigPanel.add(infoLabel, BorderLayout.LINE_START);
        generateDefaultConfigPanel.add(generateButton, BorderLayout.LINE_END);

        return generateDefaultConfigPanel;
    }

    public TextFieldWithBrowseButton getTargetProjectPathTextField() {
        return targetProjectPathTextField;
    }
}