package preferences.FQNTableUI;

import javax.swing.*;
/**
 * Created by Nils-Hendrik Berger
 */
public class ContainsFilter extends RowFilter<Object, Object>{

    private String filterValue= "";

    public ContainsFilter(final String filterValue)
    {
        this.filterValue = filterValue;
    }

    @Override
    public boolean include(final Entry<? extends Object, ? extends Object> entry) {
       if(filterValue.isEmpty())
       {
           return true;
       }
       for(int i = 0;  i<entry.getValueCount(); i++)
       {
           if(entry.getStringValue(i).toLowerCase().contains(filterValue))
               return true;
       }
       return false;
    }
    public void setFilterValue(final String filterValue)
    {
        this.filterValue = filterValue;
    }
}
