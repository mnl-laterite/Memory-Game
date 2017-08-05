
package memoryclone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * The game table. 
 * Draws the game pieces on the table and updates the graphics.   
 */
public class GamePieces extends Canvas {
    
    private static final Dimension dim = new Dimension(640,640); 
    private static final int imageNum = 13;
    
    private Image[] imageList; /* the game piece sprite list */
    private int[][] map; /* map used for the sprite locations on the table */
    private int gridSize; /* table grid dimension corresp. to the used map */
    
    private int boxSize; /* dimension of a location on the game table grid */
    private int gapSize; /* margins between a game sprite and the box edge */
    private int imageSize; /* dimension of a game piece sprite/image */
    
    private boolean isFinalScreen; /* condition for drawing end-game screen */
    
    public GamePieces() {
        
        isFinalScreen = false;
        
        super.setBackground(new Color(241,239,239));
        super.setSize(dim);
        
        imageList = new Image[imageNum];
        
        for (int i=0; i < imageNum; ++i)
            imageList[i] = new ImageIcon( getClass().getResource("/images/"+ i +".png")).getImage();
        
        repaint();
    }
    
    /**
     * The method that draws the game table.
     * Draws the default face-down sprite (image 0) if the corresponding point
     * in the game map is negative, or the appropriate sprite corresponding 
     * to the point's registered value in the game map if it is positive.
     * Draws the end-game screen if all pieces have been eliminated. 
     */
    @Override
    public void paint (Graphics g) {
        
        int i,j; /* temp variables for the position of a sprite on the table */
        
        if (isFinalScreen) {
            
            g.setColor(Color.black);
            g.drawString("You've found all the pairs!", dim.width/3,dim.height/2);
            
            return;
        }
        
        for (i=0; i < gridSize; ++i)
            for (j=0; j < gridSize; ++j) {
                
                if (map[i][j] < 0)
                    g.drawImage(imageList[0], 
                                i*boxSize + gapSize,
                                j*boxSize + gapSize,
                                imageSize, imageSize,
                                this);
                if (map[i][j] > 0)
                    g.drawImage(imageList[map[i][j]],
                                i*boxSize + gapSize,
                                j*boxSize + gapSize,
                                imageSize, imageSize,
                                this);
        } 
    }
    
    /*
     * Changes the value of the end-game screen condition.
     */
    public void isFinalScreen (boolean isFinalScreen) {
        
        this.isFinalScreen = isFinalScreen;
    }
    
    /*
     * Redraws the game table and makes the program wait for ms milliseconds.
     */
    public void updateDisplay (int ms) {
        
        repaint();  
        try {
            
            Thread.sleep(ms);
        }
        catch(InterruptedException e) {
            
            Thread.currentThread().interrupt();
        }
    }
    
    /*
     * Initializes game table scale. 
     */
    public void setGridSize (int gridSize) {
        
        this.gridSize = gridSize;
        boxSize = dim.width / gridSize;
        gapSize = gridSize == 4 ? 5 : gridSize == 5 ? 4 : 20;
        imageSize = boxSize - 2*gapSize;
    }
    
    /*
     * Initializes the game map which is followed when drawing the game table. 
     */
    public void setMap (int[][] map) {
        
        this.map = map;
    }
    
    @Override
    public Dimension getPreferredSize() {
        
        return dim;
    }

}