package preferences;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.text.StringTokenizer;
import com.intellij.util.ui.JBEmptyBorder;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nils-Hendrik Berger on 30.12.16.
 */
public class TabListCellRenderer extends JBLabel implements ListCellRenderer {
    protected static JBEmptyBorder noFocusBorder;
    protected FontMetrics fontMetrics;
    protected Insets insets = new Insets(0,0,0,0);

    protected int defaultTab = 50;
    protected int[] tabs = null;

    public TabListCellRenderer()
    {
        super();
        noFocusBorder = new JBEmptyBorder(1,1,1,1);
        setOpaque(true);
        setBorder(noFocusBorder);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        setText(value.toString());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getBackground());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

        return this;
    }

    public void setDefaultTab(int defaultTab)
    {
        this.defaultTab = defaultTab;
    }

    public int getDefaultTab()
    {
        return defaultTab;
    }

    public void setTabs(int[] tabs)
    {
        this.tabs = tabs;
    }
    public int[] getTabs()
    {
        return tabs;
    }
    public int getTabs(int index)
    {
        if(tabs == null)
            return defaultTab * index;
        int len = tabs.length;
        if(index >=0 && index<len)
            return tabs[index];
        return tabs[len-1] + defaultTab * (index - len +1);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        fontMetrics = g.getFontMetrics();
        getBorder().paintBorder(this, g, 0, 0, getWidth(),getHeight());

        g.setColor(getForeground());
        g.setFont(getFont());
        insets = getInsets();
        int x = insets.left;
        int y = insets.top + fontMetrics.getAscent();

        StringTokenizer st = new StringTokenizer(getText(), "\t");
        while (st.hasMoreTokens())
        {
            String sNext = st.nextToken();
            g.drawString(sNext, x, y);
            x +=fontMetrics.stringWidth(sNext);

            if(!st.hasMoreTokens())
                break;
            int index = 0;
            while(x>= getTabs(index))
            {
                index++;
            }
            x = getTabs(index);
        }
    }
}
