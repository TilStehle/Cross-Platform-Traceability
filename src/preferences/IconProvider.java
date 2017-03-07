package preferences;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by Gerrit Greiert on 19.12.16.
 */
public class IconProvider {

    public static Icon TRACEABILITY_ICON = IconLoader.getIcon("/icons/icon_traceability.png");

    public static Icon MAPPING_ADD = AllIcons.ToolbarDecorator.Add;
    public static Icon MAPPING_EDIT = AllIcons.ToolbarDecorator.Edit;
    public static Icon MAPPING_DELETE = AllIcons.ToolbarDecorator.Remove;

    public static Icon LINK_CLASS = AllIcons.Nodes.Class;;
    public static Icon LINK_CONSTRUCTOR = AllIcons.Nodes.ClassInitializer;;
    public static Icon LINK_ENUM = AllIcons.Nodes.Enum;
    public static Icon LINK_FIELD = AllIcons.Nodes.Field;;
    public static Icon LINK_INTERFACE = AllIcons.Nodes.Interface;;
    public static Icon LINK_METHOD = AllIcons.Nodes.Method;
    public static Icon LINK_PACKAGE = AllIcons.Nodes.Package;

}
