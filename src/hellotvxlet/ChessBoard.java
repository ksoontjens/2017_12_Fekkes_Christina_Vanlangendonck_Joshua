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

    int xoff=50; // offset vakje (breedte)
    int yoff=50;
    
    int curx=0; // welk vakje x je bent
    int cury=0; // vakje y
    
    int takenX; // onthouden waar de steen genomen is
    int takenY;
    
    // Alle stenen van de speler
    int[][] player1Array = new int[10][10];
    int[][] player2Array = new int[10][10];
    
    // Deze stenen kunnen slaan dus hier MOET er 1 van verplaatst worden
    int[][] moveArray = new int[10][10];
    
    boolean taken = false; // true --> steen moet geplaatst worden; false --> steen kan opgepakt worden
    boolean hit = false; // om score van de speler te verhogen, wordt tot nu toe nog niks mee gedaan
    boolean canHit = false; // om te checken of te speler aan beurt kan slaan, is verplicht
    
    boolean pressEnter = false;
    
    int turnPlayer = 1; // speler die aan beurt is, kan 1 of 2 zijn
    
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
    
    public void cleanMoveArray()
    {
        for(int x=0; x < moveArray.length; x++)
        {
            for(int y = 0; y < moveArray.length; y++)
            {
                moveArray[x][y] = 0;
            }
        }
    }
    
    public void paint(Graphics g)
    {
        System.out.println("================== PAINT ===================");
        for (int x=0;x<10;x++)
        {
            for (int y=0;y<10;y++)
            {
                if ((x+y)%2==0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    if(player1Array[x][y] == 1) {
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            System.out.println("------------ YELLOW ------------");
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            
                            player1Array[curx][cury]=1;
                            
                            System.out.println("curx: " + curx);
                            System.out.println("cury: " + cury);
                            taken = false;
                        }
                        else
                        {
                            //g.setColor(Color.BLUE);
                            //g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            //g.setColor(Color.WHITE);
                        }
                    }
                    else if(player2Array[x][y] ==1)
                    {
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            System.out.println("------------ YELLOW ------------");
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            
                            player2Array[curx][cury]=1;
                            
                            System.out.println("curx: " + curx);
                            System.out.println("cury: " + cury);
                            taken = false;
                        }
                        else
                        {
                            System.out.println("- RED -");
                            g.setColor(Color.RED);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                    }
                    else { } 
                }
                else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    if(player1Array[x][y] == 1) 
                    {
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            System.out.println("------------ YELLOW ------------");
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            
                            player1Array[curx][cury]=1;
                            
                            System.out.println("curx: " + curx);
                            System.out.println("cury: " + cury);
                            taken = false;
                        }
                        else
                        {
                             g.setColor(Color.BLUE);
                                g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                       
                        //g.setColor(Color.WHITE);
                    }
                    else if(player2Array[x][y] ==1)
                    {
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            System.out.println("------------ YELLOW ------------");
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            
                            player2Array[curx][cury]=1;
                            
                            System.out.println("curx: " + curx);
                            System.out.println("cury: " + cury);
                            taken = false;
                        }
                        else
                        {
                            g.setColor(Color.RED);
                            g.fillOval((x*50+xoff), y*50+xoff, 40, 40);
                        }
                       
                    }
                    else {  }
                } 
            }     
        }
        g.setColor(Color.GREEN);
        g.drawRect(curx*50+xoff, cury*50+yoff, 50, 50); //teken selectievakje
       
        cleanMoveArray();
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
       pressEnter = false;
       if (e.getType()==HRcEvent.KEY_PRESSED)
       {
           if (e.getCode()==HRcEvent.VK_RIGHT && curx < 9) curx++; //als je pijltje naar rechts druk, volgend vak
           else if (e.getCode()==HRcEvent.VK_LEFT && curx > 0) curx--;
           else if (e.getCode()==HRcEvent.VK_DOWN && cury < 9) cury++;
           else if (e.getCode()==HRcEvent.VK_UP && cury > 0) cury--;
           
           else if(e.getCode()==HRcEvent.VK_ENTER && taken == false)
           {
               System.out.println("OPNIEUW CANHIT CHECKEN!!");
               
               if(turnPlayer==1)
               { // player 1 turn
                  if(player1Array[curx][cury]==1)
                  {
                      canHit(player1Array, player2Array);
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
                        canHit(player2Array, player1Array);
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
               pressEnter = true;
           }
           else if(e.getCode()==HRcEvent.VK_ENTER && taken==true)
           {
               if(turnPlayer==1)
               {
                   moves(player1Array, player2Array, 1);
                   
                   if(hit)
                   {
                       // Score player 1 verhogen
                       // Lukt me nog niet, ik heb wel labels aangemaakt in HelloTVXlet.java
                       hit = false;
                   }
               }
               else if(turnPlayer==2)
               {
                   moves(player2Array, player1Array, -1);
                   
                   if(hit)
                   {
                       // Score player 2 verhogen
                       // Lukt me nog niet, ik heb wel labels aangemaakt in HelloTVXlet.java
                       hit = false;
                   }
               }
               pressEnter = true;
           }
           this.repaint();
       }
    }
    
    // boolean true --> player can hit opponent
    // boolean false --> player can't hit opponent
    public void canHit(int[][] ownArray, int[][]opponentArray)
    {
        cleanMoveArray();
        canHit = false;
        for(int x = 0; x < ownArray.length; x++)
        {
            for(int y = 0; y < ownArray.length; y++)
            {
                if(ownArray[x][y]==1)
                {
                    if(x>1 && y >1) // binnen het bord blijven
                    {
                        // Check of er een blokje van de tegenstander rondligt en daarachter een leeg vakje is
                        if(opponentArray[x-1][y-1]==1 && ownArray[x-2][y-2]==0 && opponentArray[x-2][y-2]==0) // links boven
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            /* TODO
                             * Als speler aan beurt is checken of canHit true is
                             * als hij true is mogen enkel de stenen in de nieuwe array verplaatst worden
                             * en als hij false is dan mag elke steen verplaatst worden
                             * */
                            System.out.println("x&y bewegen: " + x + " & " + y);
                            canHit = true;
                        }
                    }
                    if(x<9 && y>1)
                    {
                        if(opponentArray[x+1][y-1]==1 && ownArray[x+2][y-2]==0 && opponentArray[x+2][y-2]==0) // rechts boven
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            
                            System.out.println("x&y bewegen: " + x + " & " + y);
                            canHit = true;
                        } 
                    }
                    if(x>1 && y<9)
                    {
                        if(opponentArray[x-1][y+1]==1 && ownArray[x-2][y+2]==0 && opponentArray[x-2][y+2]==0) // links onder
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            
                            System.out.println("x&y bewegen: " + x + " & " + y);
                            canHit = true;
                        }
                    }
                    if(x<9 && y<9)
                    {
                      if(opponentArray[x+1][y+1]==1 && ownArray[x+2][y+2]==0 && opponentArray[x+2][y+2]==0) // rechts onder
                      {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                          
                            System.out.println("x&y bewegen: " + x + " & " + y);
                            canHit = true;
                      }
                    }
                }
            }
        }
    }
    
    public void moves(int[][] ownArray, int[][] opponentArray, int offset)
    {
       //System.out.println("takenX: " + takenX);
       //System.out.println("takenY: " + takenY);
       //System.out.println("curx: " + curx);
       //System.out.println("cury: " + cury);
        
       // Niet buiten het veld en er het veld moet leeg zijn
       if(curx < 10 && curx >= 0 && cury < 10 && cury >=0 && player1Array[curx][cury]==0 && player2Array[curx][cury]==0)
       {
           
           System.out.println("============ MOVES ===========");
           System.out.println("canHit: " + canHit);
           
           if(canHit) // je moet slaan
           {
               System.out.println("SLAAN IS VERPLICHT! U KUNT SLAAN!");
               if(curx==takenX && cury==takenY) // Damstuk toch op de zelfde plaats willen laten staan
               {
                   ownArray[curx][cury] = 1;
                   taken = false;
               }
               else if(curx==takenX+2 && cury==takenY+2) // Rechts onder slaan
               {
                   if(opponentArray[takenX+1][takenY+1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX+1][takenY+1]=0;
                       ownArray[curx][cury] = 1;
                       taken = false;
                       hit = true;
                       System.out.println("Speler mag nog eens want hij heeft rechts onder geslagen.");
                       cleanMoveArray();
                   }
               }
               else if(curx==takenX+2 && cury==takenY-2) // Rechts boven slaan
               {
                   if(opponentArray[takenX+1][takenY-1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX+1][takenY-1]=0;
                       ownArray[curx][cury] = 1;
                       taken = false;
                       hit = true;
                       System.out.println("curx,cury: " +curx+ " & " + cury);
                       System.out.println("Speler mag nog eens want hij heeft rechts boven geslagen.");
                       cleanMoveArray();
                   }
               }
               else if(curx==takenX-2 && cury==takenY+2) // Links onder slaan
               {
                   if(opponentArray[takenX-1][takenY+1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX-1][takenY+1]=0;
                       ownArray[curx][cury] = 1;
                       taken = false;
                       hit = true;
                       System.out.println("Speler mag nog eens want hij heeft links onder geslagen.");
                       cleanMoveArray();
                   }
               }
               else if(curx==takenX-2 && cury==takenY-2) // Links boven onderslaan
               {
                   if(opponentArray[takenX-1][takenY-1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX-1][takenY-1]=0;
                       ownArray[curx][cury] = 1;
                       taken = false;
                       hit = true;
                       System.out.println("Speler mag nog eens want hij heeft links boven geslagen.");
                       cleanMoveArray();
                   }
               }
           }
           else // 1 schuine stap zetten
           {
               System.out.println("U MAG EEN GEWONE ZET ZETTEN");
               System.out.println("curx: " + curx);
               System.out.println("cury: " + cury);
               System.out.println("takenX: " + takenX);
               System.out.println("takenY: " + takenY);
               System.out.println("offset: " + offset);
               if(curx==takenX+1 && cury==takenY+offset) // Damstuk 1 stap rechts schuin zetten
               {
                   System.out.println("Zet schuin rehts");
                   ownArray[curx][cury] = 1;
                   taken = false;
                   switchPlayer();
               }
               else if(curx==takenX-1 && cury==takenY+offset) // Damstuk 1 stap links schuin zetten
               {
                   System.out.println("Zet schuin links");
                   ownArray[curx][cury] = 1;
                   taken = false;
                   switchPlayer();
               }
               else if(curx==takenX && cury==takenY) // Damstuk toch op de zelfde plaats willen laten staan
               {
                   ownArray[curx][cury] = 1;
                   taken = false;
               }
           } 
       } 
    }
    
    /*
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
    }*/
}
