
package memoryclone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * The GUI of the game.
 */
public class GameEnvironment extends JPanel {
    
    private Settings settings; /* controls for the game's mode types */
    private GamePieces pieces; /* the game's pieces (drawn on an awt.Canvas) */
    private Dimension dim = new Dimension(640,680); /* window dimensions */
    
    JRadioButton[] buttons; /* settings buttons */
    JButton reset; /* ------------ reset button */
    GameSession session; /* game's logic module */
        
    public GameEnvironment() {
        
        setBackground(new Color(241,239,239));
        setSize(dim);
        
        buttons = new JRadioButton[3];
        buttons[0] = new JRadioButton("Easy"); /* for the 3 by 3 game mode */
        buttons[1] =  new JRadioButton("Default"); /* --- 4 by 4 game mode */
        buttons[2] = new JRadioButton("Hard"); /* --------5 by 5 game mode */
        reset = new JButton("Reset");
        pieces = new GamePieces();
        
        session = new GameSession(this); 
        pieces.setMap(session.getMap()); 
        pieces.setGridSize(session.getMode()); 
        
        settings = new Settings(buttons,reset);     
        
        setLayout(new BorderLayout());
        add(pieces,BorderLayout.NORTH); 
        add(settings,BorderLayout.SOUTH); 
                
    }
    
    @Override
    public Dimension getPreferredSize() {
        
        return dim;
    }
    
    /*
     * Gets the game table dimensions.  
     */
    public Dimension getPiecesSize() {
        
        return pieces.getPreferredSize();
    }
    
    /*
     * Initializes a mouse listener for the user's actions on the game table.
     */
    public void setListener (MouseListener L) {
        
        pieces.addMouseListener(L);
    }
    
    /*
     * Redraws the game table and makes the program wait for ms milliseconds.  
     */
    public void updateDisplay(int ms) {
        
        pieces.updateDisplay(ms);
    }
    
    /*
     * Resets the game using the (new) settings.
     */
    public void reset() {
        
        pieces.setGridSize(session.getMode());
        pieces.isFinalScreen(false);
        pieces.updateDisplay(0);
    }
    
    /*
     * Requests the drawing of the end-game screen.  
     */
    public void gameOver() {
        
        pieces.isFinalScreen(true);
        pieces.updateDisplay(0);
    }
        
}
