package mappings;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static mappings.MappingType.METHOD;

/**
 * Created by Gerrit Greiert on 17.10.16.
 */
public class MappingEditorDialog extends DialogWrapper {

    private final Project _project;
    private final String[] typeOptions = {"Type mapping", "Method mapping", "Modifier mapping"};
    private final String dialogTitle = "J2Swift mapping editor";

    private JComboBox typeComboBox;
    private JTextField javaTextField;
    private JTextField swiftTextField;
    private JCheckBox noGenCheckBox;

    protected MappingEditorDialog(@Nullable Project project) {

        super(project);
        _project = project;
        setTitle(dialogTitle);
        init();
    }

    protected MappingEditorDialog(@Nullable Project project, MappingNode mappingNode){

        super(project);
        _project = project;
        setTitle(dialogTitle);
        init();
        prefillFields(mappingNode);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        JPanel editorPanel = new JPanel(new BorderLayout(5,5));

        JPanel labelColumn = new JPanel(new GridLayout(4,1,5,5));
        JPanel fieldsColumn = new JPanel(new GridLayout(4,1,5,5));

        JLabel typeLabel = new JLabel("Mapping type:");
        JLabel javaLabel = new JLabel("Java expression:");
        JLabel swiftLabel = new JLabel("Swift expression:");
        JLabel noGenLabel = new JLabel("Remove generics:");

        typeComboBox = new ComboBox(typeOptions);
            typeComboBox.setSelectedIndex(0);
        javaTextField = new JTextField();
        swiftTextField = new JTextField();
        noGenCheckBox = new JCheckBox("-noGen");

        labelColumn.add(typeLabel);
        labelColumn.add(javaLabel);
        labelColumn.add(swiftLabel);
        labelColumn.add(noGenLabel);

        fieldsColumn.add(typeComboBox);
        fieldsColumn.add(javaTextField);
        fieldsColumn.add(swiftTextField);
        fieldsColumn.add(noGenCheckBox);

        editorPanel.add(labelColumn, BorderLayout.LINE_START);
        editorPanel.add(fieldsColumn, BorderLayout.CENTER);

        return editorPanel;
    }

    private void prefillFields(MappingNode mappingNode){

        switch (mappingNode.getMappingType()){
            case TYPE: typeComboBox.setSelectedIndex(0);
                break;
            case METHOD: typeComboBox.setSelectedIndex(1);
                break;
            case MODIFIER: typeComboBox.setSelectedIndex(2);
                break;
        }

        javaTextField.setText(mappingNode.getJavaExpression());
        swiftTextField.setText(mappingNode.getSwiftExpression());

        noGenCheckBox.setSelected(mappingNode.getNoGen());
    }

    public MappingNode getUpdatedMapping(){

        MappingType mappingType = null;
        switch (typeComboBox.getSelectedIndex()){
            case 0: mappingType = MappingType.TYPE;
                break;
            case 1: mappingType = MappingType.METHOD;
                break;
            case 2: mappingType = MappingType.MODIFIER;
                break;
        }

        return new MappingNode(mappingType, javaTextField.getText(), swiftTextField.getText(), noGenCheckBox.isSelected());
    }

    public MappingType getMappingType(){

        switch (typeComboBox.getSelectedIndex()){
            case 0: return MappingType.TYPE;
            case 1: return MappingType.METHOD;
            case 2: return MappingType.MODIFIER;
            default: return null;
        }
    }
    public String getJavaString(){
        return javaTextField.getText();
    }
    public String getSwiftString(){
        return swiftTextField.getText();
    }
    public boolean getNoGen() { return noGenCheckBox.isSelected(); }

}
