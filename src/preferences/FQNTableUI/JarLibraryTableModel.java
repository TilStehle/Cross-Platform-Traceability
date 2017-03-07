package preferences.FQNTableUI;




import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.RequiredLibraryDO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils-Hendrik Berger
 */
public class JarLibraryTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Library","Comment"};

    private static final int COLUMN_IDX_LIBRARY_PATH = 0;
    private static final int COLUMN_IDX_COMMENT = 1;

    private final List<RequiredLibraryDO> libraries;

    public JarLibraryTableModel(final List<RequiredLibraryDO> libraries)
    {
        this.libraries = new ArrayList<>(libraries);
    }
    @Override
    public int getRowCount() {
        return libraries.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //System.out.println("Row bei getValueAt" + rowIndex);
        RequiredLibraryDO libraryDO;
        synchronized (libraries) {
            if(rowIndex <=0 || rowIndex > libraries.size())
            {
                //return new RequiredLibraryDO.DeletionBuilder("/delete", "ERROR").build();
            }
            libraryDO = libraries.get(rowIndex);
        }
        if(columnIndex == COLUMN_IDX_LIBRARY_PATH)
        {
            return libraryDO.getPath();
        }
        if(columnIndex == COLUMN_IDX_COMMENT)
        {
            return libraryDO.getComment();
        }
        throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
    }

    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {

        final RequiredLibraryDO libaryDO = libraries.get(rowIndex);
        if(columnIndex == COLUMN_IDX_LIBRARY_PATH)
            libaryDO.setLibraryPath((String) value);
        else if (columnIndex == COLUMN_IDX_COMMENT)
            libaryDO.setComment((String) value);

        fireTableDataChanged();
    }

    protected List<RequiredLibraryDO> getElementsFromIndices(final int[] indices)
    {
        System.out.println("Indicies: " + indices.length);
        List<RequiredLibraryDO> requiredLibraryDOs = new ArrayList<>();
        for(int row : indices)
        {
            System.out.println(row);
            String path  = getValueAt(row,COLUMN_IDX_LIBRARY_PATH).toString();
            String comment = getValueAt(row,COLUMN_IDX_COMMENT) == null ? " " : getValueAt(row,COLUMN_IDX_COMMENT).toString();
            System.out.println(path + " " + comment + row );
            requiredLibraryDOs.add(new RequiredLibraryDO.DeletionBuilder(path,comment).build());
        }
        return requiredLibraryDOs;
    }

    protected List<RequiredLibraryDO> getModifiableJarColumns()
    {
        return libraries;
    }
    @Override
    public String getColumnName(final int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return true;
    }
}
