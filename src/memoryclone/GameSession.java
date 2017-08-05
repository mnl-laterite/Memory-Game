
package memoryclone;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import java.util.Random;

/**
 * The logic module of the game. 
 * Determines game modes, monitors progress and checks for end-game conditions. 
 */
public class GameSession implements ActionListener {
    
    private GameEnvironment environment;
    private MouseTrigger mousetrigger;
    private AbstractButton option;
    
    private static final int defaultMode = 4; /* standard game mode, 16 pieces */
    private static final int easyMode = 3; /* simpler --------------, 8 pieces */
    private static final int hardMode = 5; /* more advanced--------, 24 pieces */
    private int mode; /* current game mode */
    private int tempMode; /* temporary variable used when changing game modes */
    private int pairsTotal; /* number of pairs to be found and eliminated */
    private int pairsFound; /* number of pairs eliminated so far */
    
    /*
     * Map by which the pieces are set on the table. 
     * It holds numbers corresponding to each of the unique piece types/sprite.
     * The numbers are negative if the pieces are face-down (hidden),  
     * and positive if they are face-up. 
     * After a pair is found, the corresponding points in the map are set to 0.
     */
    private static int[][] map = new int[5][5]; 
    private int boxSize; /* dimension of a point in the map on the table */
    private int gapSize; /* margins between a game sprite and the box edge */
    private int imageSize; /* dimension of a game piece sprite/image */
    
    public GameSession (GameEnvironment env) {
        
        mode = defaultMode;
        tempMode = mode;
        
        pairsFound = 0;
        pairsTotal = (mode*mode)/2;
        shuffle(); 
        
        environment = env;
        
        /*
         * Registering game table scale for default difficulty/mode.
         */
        boxSize = environment.getPiecesSize().width / mode;
        gapSize = 5;
        imageSize = boxSize - 10;
        
        mousetrigger = new MouseTrigger();
        environment.setListener(mousetrigger);
        registerButtons(); 
        
    }
        
    /**
     * The function that effectively plays the game according to user input. 
     * Determines if a user action is "legal" and checks the game progress.
     * Ends game when all the pieces have been eliminated.
     */
    public void play() {
        
        int i,j; /* temp variables for the position of an action on the map */
        Dimension coordinates; 
        
        while (pairsTotal - pairsFound > 0) {
            
            coordinates = mousetrigger.waitForCoordinates(); 
            i = coordinates.width / boxSize;
            j = coordinates.height / boxSize;
            
            if (isOnImage(coordinates,i,j))
                
                map[i][j] *= -1; 
            
            else {
                
                while (!(isOnImage(coordinates,i,j))) { 
                    
                    coordinates = mousetrigger.waitForCoordinates();
                    i = coordinates.width / boxSize;
                    j = coordinates.height / boxSize;
                }
                
                map[i][j] *= -1; 
            }
            
            environment.updateDisplay(0); 
            
            coordinates = mousetrigger.waitForCoordinates();
            i = coordinates.width / boxSize;
            j = coordinates.height / boxSize;
            
            if (isOnImage(coordinates,i,j))
                
                map[i][j] *= -1; 
            
            else {
                
                while (!(isOnImage(coordinates,i,j))) {
                    
                    coordinates = mousetrigger.waitForCoordinates();
                    i = coordinates.width / boxSize;
                    j = coordinates.height / boxSize;
                }
                
                map[i][j] *= -1;
            }
            
            environment.updateDisplay(500);  
            
            if (pairFound()) 
                pairsFound++; 
                
            environment.updateDisplay(0);      
            
            if (pairsFound == pairsTotal) {
                
                environment.gameOver();
                pairsFound = 0;
            }
                
        }
    }
    
    /*
     * Changes game mode according to the user's option &
     * activates game reset if requested.  
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        
        option = (AbstractButton)event.getSource();
        
        if (option.equals(environment.buttons[0])) {
            tempMode = easyMode;
        }
        else
            if (option.equals(environment.buttons[2])) {
                tempMode = hardMode;
            }
            else
                if (option.equals(environment.buttons[1])) {
                    tempMode = defaultMode;
                }
        if (option.equals(environment.reset)) {
            
            mode = tempMode;
            reset(); 
        }              
    }
    
    /*
     * Begins monitoring for user input on the game settings. 
     */
    private void registerButtons() {
        
        environment.buttons[0].addActionListener(this);
        environment.buttons[1].addActionListener(this);
        environment.buttons[2].addActionListener(this);
        environment.reset.addActionListener(this);
    }
    
    /*
     * Resets the game at the user's request using the chosen game mode. 
     */
    private void reset() {
        
        pairsFound = 0;
        pairsTotal = (mode*mode)/2;
        shuffle();
        
        /*
         * Registering game table scale according to the new difficulty/mode.
         */
        boxSize = environment.getPiecesSize().width / mode;
        gapSize = mode == defaultMode ? 5 : mode == hardMode ? 4 : 20;
        imageSize = boxSize - 2*gapSize;
        
        environment.reset();
    }
    
    /*
     * Determines if an action made by the user is legal.
     * (i.e. if the click is performed on a game piece sprite)
     */
    private boolean isOnImage(Dimension d, int i, int j) {
        
        int leftBottomX = i*boxSize + gapSize;
        int leftBottomY = j*boxSize + gapSize;
        int rightTopX = leftBottomX + imageSize;
        int rightTopY = leftBottomY + imageSize;
        
        return d.width < leftBottomX ? false : 
               d.width > rightTopX ? false : 
               d.height < leftBottomY ? false : 
               d.height > rightTopY ? false : 
               true;
    }
    
    /*
     * Determines whether any pair was found during a turn.
     */
    private boolean pairFound() {
        
        int pointsSearched = 0;
        Dimension pointOne = new Dimension();
        Dimension pointTwo = new Dimension();
        boolean searching = true;
        
        /*
         * Looking for flipped pieces starting from the upper left corner.
         */
        for (int i=0; i < mode && searching; ++i)
            for(int j=0; j < mode; ++j) {
                
                if (map[i][j] > 0) {
                    
                    pointOne.setSize(i,j);
                    searching = false;
                    break;
                }
                pointsSearched++;
            }
        
        if (pointsSearched == 2*pairsTotal) /* no pieces were flipped */
            return false; 
        
        searching = true;
        
        /*
         * Looking for flipped pieces starting from the lower right corner.
         */
        for (int i=mode-1; i >= 0 && searching; --i)
            for (int j=mode-1; j >= 0; --j) {
                
                if (map[i][j] > 0) {
                    
                    pointTwo.setSize(i,j);
                    searching = false;
                    break;
                }
            }
        
        if (pointOne.width == pointTwo.width && pointOne.height == pointTwo.height)
            return false; /* the user wasted a turn by flipping over the same piece */
        
        if (map[pointOne.width][pointOne.height] == 
            map[pointTwo.width][pointTwo.height]) {
            
            map[pointOne.width][pointOne.height] = 0; 
            map[pointTwo.width][pointTwo.height] = 0; 
            
            return true;
        }
        else {
            
            map[pointOne.width][pointOne.height] *= -1; 
            map[pointTwo.width][pointTwo.height] *= -1; 
            
            return false;
        }
    } 
    
    /*
     * Shuffles the game piece locations on the map. 
     */
    private void shuffle() {
        
        Random rand = new Random();
        
        int i,j;
        int temp = pairsTotal;
        int set = 2*pairsTotal;
        int[] shuffler = new int[set]; /* temp array with sprite values */
        
        /* Filling array with values for pairs of sprites, in order. */
        for (i = 0; i < set; ++i)
            shuffler[i] = i < temp ? -i-1 : -i-1 + temp;
        
        /* Randomly rearranging the entries in the array. */
        for (i = set-1; i > 0; --i) {
            
            j = rand.nextInt(i);
            
            temp = shuffler[j];
            shuffler[j] = shuffler[i];
            shuffler[i] = temp;
        }
        
        /* Laying out the sprites on the map starting from the upper left corner. */
        temp = 0;
        for (i = 0; i < mode; ++i)
            for (j = 0; j < mode; ++j) {
                
                map[i][j] = temp < set ? shuffler[temp] : 0;
                ++temp;
            }
                
        
    }
    
    public int getMode() {
        
        return mode;
    }
    
    public int[][] getMap() {
        
        return map;
    }
}
