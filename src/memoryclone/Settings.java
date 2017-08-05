
package memoryclone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Game settings panel.
 */
public class Settings extends JPanel {
    
    private static final Dimension dim = new Dimension(640,40);
    private ButtonGroup group;
    
    public Settings(JRadioButton[] buttons, JButton b) {
        
        setBackground(new Color(241,239,239));
        setSize(dim);
        setLayout(new FlowLayout(FlowLayout.TRAILING));
        
        group = new ButtonGroup();
        
        if (buttons != null) 
            for (int i=0; i<buttons.length; ++i) {
                buttons[i].setSelected(i==1 ? true : false);
                group.add(buttons[i]);
                add(buttons[i]);
            }
        add(b);   
           
    }
    
    @Override
    public Dimension getPreferredSize() {
        
        return dim;
    }
    
}
