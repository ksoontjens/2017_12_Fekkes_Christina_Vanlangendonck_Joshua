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
    
    int turnPlayer = 1; // speler die aan beurt is, kan 1 of 2 zijn
    
    // Board set-up
    // Damstenen worden in juiste array geplaatst
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
    
    /* Vakjes van het bord krijgen de juiste kleur: wit of zwart
     * Damstenen krijgen de juiste kleur: blauw, rood of geel
     * Blauw: player 1
     * Rood: player 2
     * Geel: stenen die geslagen kunnen worden */
    public void paint(Graphics g)
    {
        System.out.println("================== PAINT ===================");
        for (int x=0;x<10;x++)
        {
            for (int y=0;y<10;y++)
            {
                if ((x+y)%2==0) { // Witte velden
                    g.setColor(Color.WHITE);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    
                    if(player1Array[x][y] == 1) {
                        // Als er geslagen kan worden en je hebt een steen opgepakt waarmee je niet kan slaan
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0){
                            // Stenen waarmee je kan slaan, geel kleuren als hulplijn
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                        else
                        {
                            g.setColor(Color.BLUE);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                    }
                    else if(player2Array[x][y] ==1){
                        // Als er geslagen kan worden en je hebt een steen opgepakt waarmee je niet kan slaan
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0){
                            // Stenen waarmee je kan slaan, geel kleuren als hulplijn
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                        else
                        {
                            g.setColor(Color.RED);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                    }
                }
                else { // Zwarte velden
                    g.setColor(Color.BLACK);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    
                    if(player1Array[x][y] == 1) 
                    {
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                        else
                        {
                             g.setColor(Color.BLUE);
                             g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                    }
                    else if(player2Array[x][y] ==1)
                    {
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            g.setColor(Color.YELLOW);
                            g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                        }
                        else
                        {
                            g.setColor(Color.RED);
                            g.fillOval((x*50+xoff), y*50+xoff, 40, 40);
                        }
                       
                    }
                } 
            }     
        }
        g.setColor(Color.GREEN);
        g.drawRect(curx*50+xoff, cury*50+yoff, 50, 50); //teken selectievakje
       
        cleanMoveArray();
    }
    
    // Pijltjes laten bewegen
    // Events aan 'Enter' gelinkt: stenen oppakken en plaatsen
    public void userEventReceived(UserEvent e) {
       if (e.getType()==HRcEvent.KEY_PRESSED)
       {
           // Events gelinkt aan de pijltjes als ze binnen het bord blijven
           if (e.getCode()==HRcEvent.VK_RIGHT && curx < 9) curx++;
           else if (e.getCode()==HRcEvent.VK_LEFT && curx > 0) curx--;
           else if (e.getCode()==HRcEvent.VK_DOWN && cury < 9) cury++;
           else if (e.getCode()==HRcEvent.VK_UP && cury > 0) cury--;
           
           // Wanneer er op 'Enter' wordt gedrukt en er is nog geen steen opgepakt
           else if(e.getCode()==HRcEvent.VK_ENTER && taken == false)
           {
               if(turnPlayer==1) // Speler 1 aan beurt
               {// Als er op de huidige positie een steen kan worden opgepakt
                  if(player1Array[curx][cury]==1)
                  {
                      canHit(player1Array, player2Array); // Check of er op het bord een steen kan slaan (verplicht!)
                      
                      // Als je kan slaan
                      if(canHit)
                      {
                          // Als de steen die je wil oppakken kan slaan, pak hem op
                          if(moveArray[curx][cury]==1)
                          {
                              player1Array[curx][cury] = 0;
                              taken = true;
                              takenX = curx;
                              takenY = cury;
                          }
                      }
                      else // Als je niet kan slaan mag je een steen naar keuze oppakken
                      {
                          player1Array[curx][cury] = 0;
                          taken = true;
                          takenX = curx;
                          takenY = cury;
                      }
                  }
               }
               else // Speler 2 aan beurt
               {
                    if(player2Array[curx][cury]==1)
                    {
                        canHit(player2Array, player1Array);
                        
                        if(canHit) // Als je kan slaan
                        {
                            // Als de steen die je wil oppakken kan slaan, pak hem op
                            if(moveArray[curx][cury]==1) // 
                            {
                                player2Array[curx][cury] = 0;
                                taken = true;
                                takenX = curx;
                                takenY = cury;
                            }
                        }
                        else // Als je niet kan slaan, pak een steen naar keuze op
                        {
                            player2Array[curx][cury] = 0;
                            taken = true;
                            takenX = curx;
                            takenY = cury;
                        }
                    }
               }
           }
           else if(e.getCode()==HRcEvent.VK_ENTER && taken==true)
           {
               if(turnPlayer==1)
               {
                   // Steen plaatsen op het veld
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
                   // Steen plaatsen op het veld
                   moves(player2Array, player1Array, -1);
                   
                   if(hit)
                   {
                       // Score player 2 verhogen
                       // Lukt me nog niet, ik heb wel labels aangemaakt in HelloTVXlet.java
                       hit = false;
                   }
               }
           }
           this.repaint();
       }
    }
    
    // Gaat alle stenen in de array van de speler aan beurt af en checkt of er een steen kan slaan
    // Past 'canHit' boolean aan
    // canHit true --> player can hit opponent
    // canHit false --> player can't hit opponent
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
                        // Links boven
                        if(opponentArray[x-1][y-1]==1 && ownArray[x-2][y-2]==0 && opponentArray[x-2][y-2]==0) // links boven
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                        }
                    }
                    if(x<9 && y>1)
                    {
                        // Rechts boven
                        if(opponentArray[x+1][y-1]==1 && ownArray[x+2][y-2]==0 && opponentArray[x+2][y-2]==0) // rechts boven
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                        } 
                    }
                    if(x>1 && y<9)
                    {
                        // Links onder
                        if(opponentArray[x-1][y+1]==1 && ownArray[x-2][y+2]==0 && opponentArray[x-2][y+2]==0) // links onder
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                        }
                    }
                    if(x<9 && y<9)
                    {
                      // Rechts onder
                      if(opponentArray[x+1][y+1]==1 && ownArray[x+2][y+2]==0 && opponentArray[x+2][y+2]==0) // rechts onder
                      {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                      }
                    }
                }
            }
        }
    }
    
    // Checkt of er op curx en cury een steen mag worden neergezet
    public void moves(int[][] ownArray, int[][] opponentArray, int offset)
    {
       // Niet buiten het veld en er het veld moet leeg zijn
       if(curx < 10 && curx >= 0 && cury < 10 && cury >=0 && player1Array[curx][cury]==0 && player2Array[curx][cury]==0)
       {
           if(canHit) // je moet slaan
           {
               if(curx==takenX+2 && cury==takenY+2) // Rechts onder slaan
               {
                   if(opponentArray[takenX+1][takenY+1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX+1][takenY+1]=0; // Steen van de tegenstander van het bord halen
                       ownArray[curx][cury] = 1; // Je eigen steen plaatsen
                       taken = false;
                       hit = true;
                       cleanMoveArray();
                   }
               }
               else if(curx==takenX+2 && cury==takenY-2) // Rechts boven slaan
               {
                   if(opponentArray[takenX+1][takenY-1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX+1][takenY-1]=0; // Steen van de tegenstander van het bord halen
                       ownArray[curx][cury] = 1; // Eigen steen plaatsen
                       taken = false;
                       hit = true;
                       cleanMoveArray();
                   }
               }
               else if(curx==takenX-2 && cury==takenY+2) // Links onder slaan
               {
                   if(opponentArray[takenX-1][takenY+1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX-1][takenY+1]=0; // Steen van de tegenstander van het bord halen
                       ownArray[curx][cury] = 1; // Eigen steen plaatsen
                       taken = false;
                       hit = true;
                       cleanMoveArray();
                   }
               }
               else if(curx==takenX-2 && cury==takenY-2) // Links boven onderslaan
               {
                   if(opponentArray[takenX-1][takenY-1]==1) // Als je een stuk van de tegenstander kan slaan
                   {
                       opponentArray[takenX-1][takenY-1]=0; // Steen van de tegenstander van het bord halen
                       ownArray[curx][cury] = 1; // Eigen steen plaatsen
                       taken = false;
                       hit = true;
                       cleanMoveArray();
                   }
               }
               else // Als de speler niet slaat met de steen dan wordt deze teruggezet om opnieuw te proberen
               {
                   ownArray[takenX][takenY] = 1;
                   taken = false;
               }
               
               // Als de speler geslagen heeft en niet meer verder kan slaan is de beurt aan de tegenstander
               canHit(ownArray, opponentArray);
               if(canHit == false && hit==true)
               {
                   switchPlayer();
               }
           }
           else if(hit == false) // Als je niet kan slaan & je hebt geen steen vast die nog moet slaan
           {
               if(curx==takenX+1 && cury==takenY+offset) // Damstuk 1 stap rechts schuin zetten
               {
                   ownArray[curx][cury] = 1;
                   taken = false;
                   switchPlayer();
               }
               else if(curx==takenX-1 && cury==takenY+offset) // Damstuk 1 stap links schuin zetten
               {
                   ownArray[curx][cury] = 1;
                   taken = false;
                   switchPlayer();
               }
               else if(curx==takenX && cury==takenY) // Damstuk toch op de zelfde plaats willen laten staan
               {
                   if(freeSpaceAroundPiece(curx,cury)==false)
                   {
                       ownArray[curx][cury] = 1;
                       taken = false;
                   }
               }
           } 
       } 
    }
    
    // Alle stenen in de moveArray worden gelijkgesteld aan 0
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
    
    // Andere speler is aan de beurt
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
    
    // Kijkt of de vakjes rond de meegegeven coordinaten leeg zijn
    // Als offset 1 is dan 
    // return true --> er zijn nog plaatsen waar de damsteen kan staan
    // return false --> de damsteen kan nergens staan
    public boolean freeSpaceAroundPiece(int x, int y)
    {
        if(turnPlayer==1)
        {
            if((player1Array[x-1][y+1]==1 || player2Array[x-1][y+1]==1) && (player1Array[x+1][y+1]==1 || player2Array[x+1][y+1]==1))
            {
                return false;
            }
        }
        else if(turnPlayer==2)
        {
            if((player1Array[x-1][y-1]==1 || player2Array[x-1][y-1]==1) && (player1Array[x+1][y-1]==1 || player2Array[x+1][y-1]==1))
            {
                return false;
            }
        }
        return true;
    }
}
