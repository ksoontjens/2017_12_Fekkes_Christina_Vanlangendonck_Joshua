/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hellotvxlet;

//import com.sun.net.ssl.internal.ssl.Debug;
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

    int xoff=50; //offset vakje (breedte)
    int yoff=50;
    
    int curx=0; //welk vakje x je bent
    int cury=0; //vakje y
    
    int[][] bordArray = new int[10][10];
    boolean taken = false;
    
    public ChessBoard()
    {
        this.setBounds(0,0,720,576); // full screen
        for(int i = 0; i<bordArray.length; i++) { //dit vult de array bij start
            for(int j = 0; j<bordArray.length; j++) {
                if(i<4 || i>5) {
                    if ((i+j)%2==1) {
                        bordArray[j][i] = 1;//zet damstukjes op juiste plaats
                    } 
                }
                else{
                    bordArray[j][i] = 0;
                }
            }
        }
    }
    
    public void paint(Graphics g)
    {
        for (int x=0;x<10;x++)
        {
            for (int y=0;y<10;y++)
            {
                if ((x+y)%2==0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    if(bordArray[x][y] == 1) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        //g.setColor(Color.WHITE);
                    }
                    else {
                        
                    } 
                }
                else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    if(bordArray[x][y] == 1) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        //g.setColor(Color.WHITE);
                    }
                    else {
                        
                    }
                } 
            }
                
        }
        g.setColor(Color.RED);
        g.drawRect(curx*50+xoff, cury*50+yoff, 50, 50); //teken selectievakje
    }

    public void userEventReceived(UserEvent e) {
       if (e.getType()==HRcEvent.KEY_PRESSED)
       {
           if (e.getCode()==HRcEvent.VK_RIGHT) curx++; //als je pijltje naar rechts druk, volgend vak
           else if (e.getCode()==HRcEvent.VK_LEFT) curx--;
           else if (e.getCode()==HRcEvent.VK_DOWN) cury++;
           else if (e.getCode()==HRcEvent.VK_UP) cury--;
           else if (e.getCode()==HRcEvent.VK_1) {
               //System.out.println(curx);
               bordArray[curx][cury] = 0;
               taken = true;
           }
           else if (e.getCode()==HRcEvent.VK_2) {
               if(taken){
                   bordArray[curx][cury] = 1;
                   taken = false;
               }
               else{} 
           }
           this.repaint();
       }
    }
}
