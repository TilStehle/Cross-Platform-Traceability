package preferences.ExtensionUI;



import de.unihamburg.masterprojekt2016.converter.j2swift.configuration.ExtensionDO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils-Hendrik Berger on 03.01.17.
 */
public class ExtensionListModel extends AbstractListModel{
    private final List<ExtensionDO> extensions;

    public ExtensionListModel(final List<ExtensionDO> extensions)
    {
        this.extensions = new ArrayList<>(extensions);
    }


    public int getSize() {
        return extensions.size();
    }

    @Override
    public ExtensionDO getElementAt(int index) {
        return extensions.get(index);
    }

    public List<ExtensionDO> getElementsFromIndices(int[] indices)
    {
        List<ExtensionDO> extensionDOs = new ArrayList<>();
        for(int i : indices)
        {
            extensionDOs.add(getElementAt(i));
        }
        return extensionDOs;
    }

    public List<ExtensionDO> getModifiableExtensionColumns()
    {
        return extensions;
    }
}
