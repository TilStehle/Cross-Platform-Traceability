package preferences.ExtensionUI;




import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.ExtensionDO;

import javax.swing.*;
import java.util.List;

/**
 * Created by macbook on 03.01.17.
 */
public class DynamicListModel extends ExtensionListModel {

    private final List<ExtensionDO> entries;


    public DynamicListModel(final List<ExtensionDO> entries) {
        super(entries);
        this.entries = entries;
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public ExtensionDO getElementAt(int index) {
        return entries.get(index);
    }

    public void addEntry(final ExtensionDO newEntry)
    {
        entries.add(newEntry);
        fireIntervalAdded(this, getSize()-1, getSize()-1);
    }
    public void addEntries(final List<ExtensionDO> newEntries)
    {
        entries.addAll(newEntries);
        fireIntervalAdded(this, getSize()-1, getSize()-1);
    }

    protected void setNewContent(final List<ExtensionDO> newEntries)
    {
        entries.clear();
        entries.addAll(newEntries);
        fireContentsChanged(this, 0, getSize()-1);
    }

    public void rowContentChanged(final int startIndex, final int endIndex)
    {
        fireContentsChanged(this, startIndex, endIndex);
    }

    public void removeElementAt(int index)
    {
        entries.remove(index);
        fireContentsChanged(this,0, getSize()-1);
    }
}
