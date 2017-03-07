package preferences.ContextAdapterUI;


import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Nils-Hendrik Berger on 14.12.16.
 */
public class ContextAdapterCreator implements Configurable{

    private ContextAdapterCreatorUI ui;
    private resourceConverter.ContextAdapterCreator contextAdapterCreator;

    public ContextAdapterCreator()
    {
        ui = new ContextAdapterCreatorUI();
        contextAdapterCreator  = resourceConverter.ContextAdapterCreator.getInstance();
        addListeners();
        createContextAdapter();
        updateTextArea();
    }
    @Nls
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
       return ui.getMainPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    private void addListeners()
    {
        ui.getStringCheckBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextArea();
            }
        });
        ui.getStringArrayCheckBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextArea();
            }
        });
        ui.getStringPluralsCheckBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextArea();
            }
        });
        ui.getAssetCheckBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTextArea();
            }
        });

    }
    private void updateTextArea()
    {
        contextAdapterCreator.addGetString(ui.getStringCheckBox().isSelected());
        contextAdapterCreator.addGetStringArray(ui.getStringArrayCheckBox().isSelected());
        contextAdapterCreator.addGetStringPlurals(ui.getStringPluralsCheckBox().isSelected());
        contextAdapterCreator.addGetAsset(ui.getAssetCheckBox().isSelected());

        try{
            File temp = new File(System.getProperty("java.io.tmpdir"));
            contextAdapterCreator.create(temp);
            File file = new File(temp + File.separator +contextAdapterCreator.FILENAME + contextAdapterCreator.SUFFIX);
            if(file.exists())
            {
                FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);

                ui.getTextArea().read(br,null);
                br.close();
                ui.getTextArea().requestFocus();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void createContextAdapter()
    {
        ui.getGenerateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destination = ui.getTargetContextAdapterTextFieldWithBrowseButton().getText();
                File destinationDirectory  = new File(destination);
                if(destinationDirectory.isDirectory())
                {
                    contextAdapterCreator.create(new File(destination));
                }
                else
                {
                    Messages.showErrorDialog("The given path is not a directory","Error");
                }
                File test = new File(destinationDirectory.getAbsolutePath() + File.separator + contextAdapterCreator.FILENAME
                        + contextAdapterCreator.SUFFIX);

                if(test.exists())
                {
                    Messages.showInfoMessage("ContextAdapter.swift was created successfully","Success");
                }
                else
                {
                    Messages.showErrorDialog("ContextAdapter.swift could not be created", "Error");
                }
            }
        });
    }
}
