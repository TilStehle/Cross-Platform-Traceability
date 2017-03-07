package preferences.FQNTableUI;



import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.RequiredLibraryDO;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nils-Hendrik Berger on 07.12.16.
 */
public class DynamicJarLibraryTableModel extends JarLibraryTableModel {
    private boolean editable = false;
    public DynamicJarLibraryTableModel(List<RequiredLibraryDO> libraries) {
        super(libraries);
    }

    public void addEntry(final RequiredLibraryDO libraryDO) {
        getModifiableJarColumns().add(libraryDO);
        final int newRowIndex = getModifiableJarColumns().size() - 1;
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }
    public void addEntries(final List<RequiredLibraryDO> librariesDO) {
        getModifiableJarColumns().addAll(librariesDO);
        fireTableDataChanged();

    }

    protected void setNewContent(final List<RequiredLibraryDO> libraryDO)
    {
        getModifiableJarColumns().clear();
        getModifiableJarColumns().addAll(libraryDO);
        fireTableDataChanged();
    }
    public void deleteContent(final RequiredLibraryDO columnToDelete)
    {
        List<RequiredLibraryDO> columns = getModifiableJarColumns();
        for(Iterator<RequiredLibraryDO> it = columns.iterator();it.hasNext();)
        {
           if(it.next().getPath().trim().contains(columnToDelete.getPath()))
           {
               it.remove();
           }
        }
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editable;
    }
    public boolean isEditable()
    {
        return editable;
    }
    public void setEditable(final boolean editable)
    {
        this.editable = editable;
        fireTableDataChanged();
    }

}
