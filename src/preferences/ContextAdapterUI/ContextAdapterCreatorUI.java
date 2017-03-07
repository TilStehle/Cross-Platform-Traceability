package preferences.ContextAdapterUI;


import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;
import preferences.ToolbarPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nils-Hendrik Berger on 14.12.16.
 */
public class ContextAdapterCreatorUI {


    private JPanel mainPanel;
    private JTextArea jTextArea;
    private JBScrollPane jScrollPane;
    private ToolbarPanel toolbarPanel;
    private JBPanel bottomPanel;

    private JButton generateButton;
    private TextFieldWithBrowseButton targetContextAdapterTextFieldWithBrowseButton;

    private JBCheckBox checkBoxGetString;
    private JBCheckBox checkBoxGetStringArray;
    private JBCheckBox checkBoxGetStringPlurals;
    private JBCheckBox checkBoxGetAsset;

    public ContextAdapterCreatorUI()
    {
        mainPanel = new JBPanel(new VerticalFlowLayout());
        mainPanel.add(generateTextArea(),VerticalFlowLayout.TOP);
        mainPanel.add(generateToolbarPanel(),VerticalFlowLayout.MIDDLE);
        mainPanel.add(generateBottomComponent());
    }

    private JBScrollPane generateTextArea() {
        jTextArea = new JTextArea(25, 50);
        jTextArea.setVisible(true);
        jTextArea.setEditable(false);
        jScrollPane = new JBScrollPane(jTextArea);
        return jScrollPane;
    }
    public ToolbarPanel generateToolbarPanel()
    {
        checkBoxGetString = new JBCheckBox("getString()", false);
        checkBoxGetStringArray = new JBCheckBox("getStringArray()", false);
        checkBoxGetStringPlurals = new JBCheckBox("addGetStringPlurals()", false);
        checkBoxGetAsset = new JBCheckBox("addGetAsset()",  false);

        return new ToolbarPanel.ToolbarPanelBuilder().setCheckBox(checkBoxGetString)
                .setCheckBox(checkBoxGetStringArray)
                .setCheckBox(checkBoxGetStringPlurals)
                .setCheckBox(checkBoxGetAsset)
                .build();
    }

    /**
     * Creating the theBottomComponent for the SplitPane
     * @return
     */
    public JBPanel generateBottomComponent()
    {
        bottomPanel = new JBPanel();


        //Title Label
        JLabel titleLabel = new JBLabel("Target directory");
        bottomPanel.add(titleLabel);

        //TextFieldWithBrowseButton
        targetContextAdapterTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        targetContextAdapterTextFieldWithBrowseButton.setEditable(false);
        try {
            targetContextAdapterTextFieldWithBrowseButton.setText(SettingsPropertyProvider.getTargetPath());
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        targetContextAdapterTextFieldWithBrowseButton.addBrowseFolderListener("J2Swift", "Select the directory for the ContextAdapter",
                null, fileChooserDescriptor);

        bottomPanel.add(targetContextAdapterTextFieldWithBrowseButton);
        bottomPanel.add(generateGenerateButton());

        return bottomPanel;
    }

    public JButton generateGenerateButton()
     {
         return generateButton = new JButton("Generate");
     }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public JBCheckBox getStringCheckBox()
    {
        return checkBoxGetString;
    }
    public JBCheckBox getStringArrayCheckBox()
    {
        return checkBoxGetStringArray;
    }
    public JBCheckBox getStringPluralsCheckBox()
    {
        return checkBoxGetStringPlurals;
    }
    public JBCheckBox getAssetCheckBox()
    {
        return checkBoxGetAsset;
    }

    public JTextArea getTextArea()
    {
        return jTextArea;
    }
    public JButton getGenerateButton()
    {
        return generateButton;
    }
    public TextFieldWithBrowseButton getTargetContextAdapterTextFieldWithBrowseButton()
    {
        return targetContextAdapterTextFieldWithBrowseButton;
    }
}
