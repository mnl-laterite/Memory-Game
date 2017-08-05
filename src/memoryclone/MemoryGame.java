
/**
 * A memory game. 
 * Goal: eliminate all the pairs off the "table."
 * A pair is eliminated when both its pieces are discovered in the same turn.
 * A turn consists of turning over 2 pieces. 
 * The pieces are randomly set in an N by N grid at the beginning of the game.
 **/

package memoryclone;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class MemoryGame {
    
    private GameSession session; /* game's logic module */
    private GameEnvironment environment; /* game's GUI */
    private String title; 
    
    public MemoryGame(String title) {
        
        this.title = title;
        
        environment = new GameEnvironment(); /* creates GUI and logic module */
        session = environment.session;
        
       SwingUtilities.invokeLater(
           new Runnable() {
               
               @Override
               public void run() {
                   
                   packGUI();
           }
           });
    
    }
    
    /*
     * Packs the game environment/GUI in a JFrame window.
     */
    private void packGUI() {
                   
        JFrame frame = new JFrame(title);
        frame.getContentPane().add(environment);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(environment.getPreferredSize());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  
        
    }    
    
    public static void main(String[] args) {
        
        MemoryGame game = new MemoryGame("Memory Game");
        game.session.play(); 
        System.exit(0);
     
    }
}
