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
    
    int[][] player1Array = new int[10][10];
    int[][] player2Array = new int[10][10];
    boolean taken = false;
    
    int turnPlayer = 1;
    
    public ChessBoard()
    {
        this.setBounds(0,0,720,576); // full screen
        for(int x = 0; x<player1Array.length; x++) { //dit vult de array bij start
            for(int y = 0; y<player1Array.length; y++) {
                if ((x+y)%2==1) {
                   if(x<4)
                   {
                       player1Array[y][x] = 1;//zet damstukjes op juiste plaats
                       player2Array[y][x] = 0;
                    
                   }
                   else if(x>5)
                   {
                       player1Array[y][x] = 0;
                       player2Array[y][x] = 1;
                   }
                   else
                   {
                       player1Array[y][x]=0;
                       player2Array[y][x]=0;
                   }
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
                    if(player1Array[x][y] == 1) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        //g.setColor(Color.WHITE);
                    }
                    else if(player2Array[x][y] ==1)
                    {
                        g.setColor(Color.RED);
                        g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                    }
                    else { } 
                }
                else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    if(player1Array[x][y] == 1) {
                        g.setColor(Color.BLUE);
                        g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        //g.setColor(Color.WHITE);
                    }
                    else if(player2Array[x][y] ==1)
                    {
                       g.setColor(Color.RED);
                       g.fillOval((x*50+xoff), y*50+xoff, 40, 40);
                    }
                    else {  }
                } 
            }     
        }
        g.setColor(Color.GREEN);
        g.drawRect(curx*50+xoff, cury*50+yoff, 50, 50); //teken selectievakje
    }
    
    public void switchPlayer()
    {
      if(turnPlayer == 1){
        System.out.println("Turn for player 2");
        turnPlayer = 2;
      }
        
      else{
        System.out.println("Turn for player 1");
        turnPlayer = 1;
      }
    }
    
    public void userEventReceived(UserEvent e) {
      
       if (e.getType()==HRcEvent.KEY_PRESSED)
       {
           if (e.getCode()==HRcEvent.VK_RIGHT) curx++; //als je pijltje naar rechts druk, volgend vak
           else if (e.getCode()==HRcEvent.VK_LEFT) curx--;
           else if (e.getCode()==HRcEvent.VK_DOWN) cury++;
           else if (e.getCode()==HRcEvent.VK_UP) cury--;
           
           else if(e.getCode()==HRcEvent.VK_1 && taken == false)
           {
               if(turnPlayer==1)
               { // player 1 turn
                  if(player1Array[curx][cury]==1)
                  {
                      System.out.println("BEZET");
                      player1Array[curx][cury] = 0;
                      taken = true;
                      takenX = curx;
                      takenY = cury;
                  }
                  else
                  {
                      System.out.println("NIET BEZET");
                  }
               }
               else
               { // player 2 turn
                   if(player2Array[curx][cury]==1)
                  {
                      System.out.println("BEZET");
                      player2Array[curx][cury] = 0;
                      taken = true;
                      takenX = curx;
                      takenY = cury;
                  }
                  else
                  {
                      System.out.println("NIET BEZET");
                  }
               }
           }
           else if(e.getCode()==HRcEvent.VK_2 && taken==true)
           {
               if(turnPlayer==1)
               {
                   // Niet buiten het veld en er het veld moet leeg zijn
                   if(curx < 10 && curx >= 0 && cury < 10 && player1Array[curx][cury]==0 && player2Array[curx][cury]==0)
                   {
                       if(curx==takenX && cury==takenY+1) // Damstuk 1 stap naar voor zetten
                       {
                           player1Array[curx][cury] = 1;
                           taken = false;
                           switchPlayer();
                       }
                       else if(curx==takenX && cury==takenY) // Damstuk toch op de zelfde plaats willen laten staan
                       {
                           player1Array[curx][cury] = 1;
                           taken = false;
                       }
                       else if(curx==takenX+2 && cury==takenY+2) // Rechts onder slaan
                       {
                           if(player2Array[takenX+1][takenY+1]==1) // Als je een stuk van de tegenstander kan slaan
                           {
                               player2Array[takenX+1][takenY+1]=0;
                               player1Array[curx][cury] = 1;
                               taken = false;
                               System.out.println("Player 1 mag nog eens want hij heeft geslagen.");
                           }
                       }
                       else if(curx==takenX-2 && cury==takenY+2) // Links onder slaan
                       {
                           if(player2Array[takenX-1][takenY+1]==1) // Als je een stuk van de tegenstander kan slaan
                           {
                               player2Array[takenX-1][takenY+1]=0;
                               player1Array[curx][cury] = 1;
                               taken = false;
                               System.out.println("Player 1 mag nog eens want hij heeft geslagen.");
                           }
                       }
                   } 
               }
               else if(turnPlayer==2)
               {
                    // Niet buiten het veld en er het veld moet leeg zijn
                   if(curx >= 0 && curx < 10 && cury >=0 && player1Array[curx][cury]==0 && player2Array[curx][cury]==0)
                   {
                       if(curx==takenX && cury==takenY-1) // Damstuk 1 stap naar voor zetten
                       {
                           player2Array[curx][cury] = 1;
                           taken = false;
                           switchPlayer();
                       }
                       else if(curx==takenX && cury==takenY) // Damstuk toch op de zelfde plaats willen laten staan
                       {
                           player2Array[curx][cury] = 1;
                           taken = false;
                       }
                       else if(curx==takenX-2 && cury==takenY-2) // Links boven slaan
                       {
                           if(player1Array[takenX-1][takenY-1]==1) // Als je een stuk van de tegenstander kan slaan
                           {
                               player1Array[takenX-1][takenY-1]=0;
                               player2Array[curx][cury] = 1;
                               taken = false;
                               System.out.println("Player 2 mag nog eens want hij heeft geslagen.");
                           }
                       }
                       else if(curx==takenX+2 && cury==takenY-2) // Rechts boven slaan
                       {
                           if(player1Array[takenX+1][takenY-1]==1) // Als je een stuk van de tegenstander kan slaan
                           {
                               player1Array[takenX+1][takenY-1]=0;
                               player2Array[curx][cury] = 1;
                               taken = false;
                               System.out.println("Player 2 mag nog eens want hij heeft geslagen.");
                           }
                           
                       }
                   } 
               }
           }
           
           this.repaint();
       }
    }
    
    public boolean allowedToDrop() {
        if(curx == takenX+1 && cury == takenY+1 || curx == takenX-1 && cury == takenY+1){ 
            //als je 1 naar voor gaat en 1 naar L or R -> allowed
            if(player1Array[curx][cury] == 0) { //als er nog geen blokje staat
                return true;
            }
            else{return false;}      
        }
        else{return false;}
    }
    
    public boolean allowedToTake() { // als er een plaats vrij is
        //nog uitzondering voor zijkanten toevoegen (out of range)
        
        if(player1Array[curx+1][cury+1] == 0 || player1Array[curx-1][cury+1] == 0){
                   return true;
        }
        else{return false;}
    }
}
