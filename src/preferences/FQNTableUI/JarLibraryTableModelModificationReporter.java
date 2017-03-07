package preferences.FQNTableUI;


import com.intellij.openapi.ui.Messages;
import com.intellij.ui.TableCellEditorWithButton;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


/**
 * Created by Nils-Hendrik Berger
 */
public class JarLibraryTableModelModificationReporter implements TableModelListener {
    @Override
    public void tableChanged(final TableModelEvent event) {
        Messages.showInfoMessage("tableChanged  (TableModelEvent)" + toString(event),"event");

        //System.out.println("tableChanged  (TableModelEvent)" + toString(event));
    }

    public String toString(final TableModelEvent event)
    {
        final String rows = "first: " + event.getFirstRow() + " / " +
                            "last row: " + event.getLastRow();
        final String column = "column" + event.getColumn();

        final String type = "type: " + typeToString(event.getType());

        return event.getClass().getCanonicalName() + "[" + rows + ", " + column + ", " + type + "]";
    }

    public String typeToString(final int type)
    {
        switch (type)
        {
            case TableModelEvent.DELETE:
                return "Delete";
            case TableModelEvent.INSERT:
                return "Insert";
            case TableModelEvent.UPDATE:
                return "Update";
        }
        return "Unkown";
    }
}
