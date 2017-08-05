
package memoryclone;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse listener which registers the user's action on the game table. 
 */
public class MouseTrigger extends MouseAdapter {
    
    private Dimension position;
    private final Object lock;
    private boolean onStandby;
   
    public MouseTrigger() {
        
        position = new Dimension();
        lock = new Object();
        
    }
    
    @Override
    public void mouseClicked (MouseEvent event) {
        
        position.width = event.getX();
        position.height = event.getY();
        onStandby = false;
        
        synchronized(lock) {
            
            lock.notifyAll();
        }
    }
    
    /*
     * Waits for the user's (click) action on the game table and registers
     * the coordinates at which the action was performed. 
     */
    public Dimension waitForCoordinates() {
        
        onStandby = true;
        
        try {
            synchronized(lock) {
                while (onStandby == true) {
                    lock.wait();
                }
            }
        }
        catch(InterruptedException excp) {
        }
            
        return position;
    }    
    
}
