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
    
    int takenX; //onthouden waar de steen genomen is
    int takenY;
    
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
                    else { } 
                }
                else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    if(bordArray[x][y] == 1) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        //g.setColor(Color.WHITE);
                    }
                    else {  }
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
           
           else if (e.getCode()==HRcEvent.VK_1 && taken != true && allowedToTake() == true) {//als je f1 drukt opnemen, maar 1 tegelijk
               //System.out.println(curx);
               bordArray[curx][cury] = 0;
               taken = true;
               takenX = curx;
               takenY = cury;
           }// we gaan een ondersheid moeten maken tussen player 1 die boven begint en 
           //player 2 onderaan, want nu is de check voor waar je de blokjes mag neerzetten alleen
           //werkend voor player1. Ook onderscheid nodig zodat player1 geen blokjes van 2 kan verplaatsen.
           else if (e.getCode()==HRcEvent.VK_2) {//als je f2 drukt dam terugleggen
               System.out.println(cury);
               if(taken && allowedToDrop() == true){
                   bordArray[curx][cury] = 1;
                   taken = false;
               }
               else{} 
           }
           this.repaint();
       }
    }
    
    public boolean allowedToDrop() {
        if(curx == takenX+1 && cury == takenY+1 || curx == takenX-1 && cury == takenY+1){ 
            //als je 1 naar voor gaat en 1 naar L or R -> allowed
            if(bordArray[curx][cury] == 0) { //als er nog geen blokje staat
                return true;
            }
            else{return false;}      
        }
        else{return false;}
    }
    
    public boolean allowedToTake() { // als er een plaats vrij is
        //nog uitzondering voor zijkanten toevoegen (out of range)
        if(bordArray[curx+1][cury+1] == 0 || bordArray[curx-1][cury+1] == 0){
                   return true;
        }
        else{return false;}
    }
}
