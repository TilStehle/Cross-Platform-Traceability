package preferences;

import com.google.common.collect.Lists;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import preferences.ContextAdapterUI.ContextAdapterCreatorUI;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Nils-Hendrik Berger on 08.01.17.
 */
public class ToolbarPanel extends JBPanel {
    private java.util.List<JBCheckBox> checkBoxList;

    public static class ToolbarPanelBuilder {
        private java.util.List<JBCheckBox> checkBoxList = Lists.newArrayList();

        public ToolbarPanelBuilder() {

        }
        public ToolbarPanelBuilder setCheckBox(JBCheckBox checkBox)
        {
            this.checkBoxList.add(checkBox);
            return this;
        }
        public ToolbarPanel build() {
            return new ToolbarPanel(this);
        }
    }

    private ToolbarPanel(ToolbarPanelBuilder builder)
    {
        this.checkBoxList = builder.checkBoxList;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        addCheckboxes();
    }
    private void addCheckboxes()
    {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for(JBCheckBox checkBox : this.checkBoxList)
        {
            toolBar.add(checkBox);
        }
        add(toolBar, BorderLayout.NORTH);
    }
}