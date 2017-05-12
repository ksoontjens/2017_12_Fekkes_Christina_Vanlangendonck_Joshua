/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hellotvxlet;

import java.awt.Color;
import java.awt.Graphics;
import org.bluray.ui.event.HRcEvent;
import org.dvb.event.UserEvent;
import org.dvb.event.UserEventListener;
import org.havi.ui.HComponent;

/**
 *
 * @author student
 */
public class ChessBoard extends HComponent implements UserEventListener {

    int xoff=100; //offset vakje (breedte)
    int yoff=100;
    
    int curx=0; //welk vakje x je bent
    int cury=0; //vakje y
    
    public ChessBoard()
    {
        this.setBounds(0,0,720,576); // full screen
    }
    
    public void paint(Graphics g)
    {
        for (int x=0;x<8;x++)
        {
            for (int y=0;y<8;y++)
            {
                if ((x+y)%2==0) g.setColor(Color.WHITE); 
                else g.setColor(Color.BLACK);
                
                g.fillRect(x*50+xoff, y*50+yoff, 50, 50);

            }
                
        }
        g.setColor(Color.BLUE);
        g.drawRect(curx*50+xoff, cury*50+yoff, 50, 50);
    }

    public void userEventReceived(UserEvent e) {
       if (e.getType()==HRcEvent.KEY_PRESSED)
       {
           if (e.getCode()==HRcEvent.VK_RIGHT) curx++; //als je pijltje naar rechts druk, volgend vak
           else if (e.getCode()==HRcEvent.VK_LEFT) curx--;
           else if (e.getCode()==HRcEvent.VK_DOWN) cury++;
           else if (e.getCode()==HRcEvent.VK_UP) cury--;
           
           this.repaint();
       }
    }
}
