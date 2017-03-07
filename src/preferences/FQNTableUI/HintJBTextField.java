package preferences.FQNTableUI;

import com.intellij.codeInsight.javadoc.ColorUtil;
import com.intellij.ide.ui.UISettings;
import com.intellij.ui.ColorChooser;
import com.intellij.ui.Colored;
import com.intellij.ui.JBColor;
import com.intellij.ui.UI;
import com.intellij.ui.components.JBTextField;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created Nils-Hendrik Berger
 */
public class HintJBTextField extends JBTextField implements FocusListener{
    private final String hint;
    private boolean showingHint;

    public HintJBTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
        setBackground(new Color(0,0,0,0));
        setOpaque(false);

    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }
    @Override public void updateUI() {
        super.updateUI();
        setBorder(new RoundedCornerBorder());
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }

    @Override protected void paintComponent(Graphics g) {
        if(!isOpaque() && getBorder() instanceof RoundedCornerBorder) {

            g.setColor(getBackground());
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                   0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
        }
        super.paintComponent(g);
    }


}
class RoundedCornerBorder extends AbstractBorder {
    private static final Color ALPHA_ZERO = new Color(0x0, true);
    @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape border = getBorderShape(x, y, width - 1, height - 1);
        g2.setPaint(new Color(0,0,0,0));
        Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
        corner.subtract(new Area(border));
        g2.fill(corner);
        g2.setPaint(Color.GRAY);
        g2.draw(border);
        g2.dispose();
    }
    public Shape getBorderShape(int x, int y, int w, int h) {
        int r = h; //h / 2;
        return new RoundRectangle2D.Double(x, y, w, h, r, r);
    }
    @Override public Insets getBorderInsets(Component c) {
        return new Insets(4, 8, 4, 8);
    }
    @Override public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(4, 8, 4, 8);
        return insets;
    }
}