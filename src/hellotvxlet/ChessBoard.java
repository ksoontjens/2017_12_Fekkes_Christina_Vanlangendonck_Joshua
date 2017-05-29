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
import java.awt.Image;

/**
 *
 * @author student
 */
public class ChessBoard extends HComponent implements UserEventListener {

    int xoff=50; // offset vakje (breedte)
    int yoff=50;
    
    int curx=0; // huidig vakje dat geselecteerd is
    int cury=0;
    
    int takenX; // coordinaten van de opgepakte steen
    int takenY;
    
    Image Player1;
    Image Player2;
 
    private HelloTVXlet helloTvXlet = new HelloTVXlet();
    
    // Alle stenen van de speler
    int[][] player1Array = new int[10][10];
    int[][] player2Array = new int[10][10];
    
    // Deze stenen kunnen slaan dus hier MOET er 1 van verplaatst worden
    int[][] moveArray = new int[10][10];
    // Plaatsen waar de dam-steen mag gezet worden
    int[][] possibleMoves = new int[10][10];
    
    boolean taken = false; // true: steen is opgepakt; false: steen is neergezet
    boolean hit = false; // om score van de speler te verhogen als er geslagen is
    boolean canHit = false; // om te checken of te speler aan beurt kan slaan, is verplicht
    boolean tookDam = false; // true: dam is opgepakt ipv gewone steen
    
    int turnPlayer = 1; // speler die aan beurt is, kan 1 of 2 zijn
    int hitCounter = 0; // hoevaak een dam heeft geslagen, om score bij te houden

    // Board set-up
    // Damstenen worden in juiste array geplaatst
    public ChessBoard()
    {
        Player1=this.getToolkit().getImage("player1.png");
        System.out.println(Player1.getHeight(this));//laten staan anders werkt het niet, zeer vreemd
        Player2=this.getToolkit().getImage("player2.png");
        System.out.println(Player2.getHeight(this));//laten staan anders werkt het niet, zeer vreemd
        
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
     * Geel: stenen die kunnen slaan */
    public void paint(Graphics g)
    {
        g.drawImage(Player1, 610, 50,null);
        g.drawImage(Player2, 610, 350,null);
        
        for (int x=0;x<10;x++) // Doorloop het volledige veld
        {
            for (int y=0;y<10;y++)
            {
                if ((x+y)%2==0) { // Witte velden
                    g.setColor(Color.WHITE);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                }
                else { // Zwarte velden
                    g.setColor(Color.BLACK);
                    g.fillRect(x*50+xoff, y*50+yoff, 50, 50);
                    
                    if(player1Array[x][y] != 0) // Als het veld bezet is door speler 1
                    {   // Als er geslagen kan worden en de juiste steen wordt niet opgepakt
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            g.setColor(Color.YELLOW); // Met geel aanduiden welke stenen kunnen slaan
                            if(player1Array[x][y]==2) g.fillRect(x*50+xoff, y*50+yoff,40, 40);
                            else if(player1Array[x][y]==1) g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            
                            helloTvXlet.changeMessage("Speler 1: slaan is verplicht!");
                        }
                        else if(player1Array[x][y]==2) // dam stenen worden vierkant
                        {
                            g.setColor(Color.BLUE);
                            g.fillRect(x*50+xoff+4, y*50+xoff+4, 40, 40);
                        }
                        else // gewone stenen van speler 1 worden blauw
                        {
                             g.setColor(Color.BLUE);
                             g.fillOval(x*50+xoff+4, y*50+xoff+4, 40, 40);
                        }
                    }
                    else if(player2Array[x][y] !=0)
                    {   // Als er geslagen kan worden en de juiste steen wordt niet opgepakt
                        if(moveArray[x][y] == 1 && moveArray[curx][cury]==0)
                        {
                            g.setColor(Color.YELLOW); // Met geel aanduiden welke stenen kunnen slaan
                            if(player2Array[x][y]==2) g.fillRect(x*50+xoff, y*50+xoff, 40, 40);
                            else if(player2Array[x][y]==1) g.fillOval(x*50+xoff, y*50+xoff, 40, 40);
                            
                            helloTvXlet.changeMessage("Speler 2: slaan is verplicht!");
                        }
                        else if(player2Array[x][y]==2) //dam stenen worden vierkant
                        {
                            g.setColor(Color.RED);
                            g.fillRect(x*50+xoff+4, y*50+xoff+4, 40, 40);
                        }
                        else // gewone stenen van player 2 worden rood
                        {
                            g.setColor(Color.RED);
                            g.fillOval(x*50+xoff+4, y*50+xoff+4, 40, 40);
                        }
                    }
                }
            }    
        }
        g.setColor(Color.GREEN);
        g.drawRect(curx*50+xoff, cury*50+yoff, 50, 50); //teken selectievakje
   
        cleanArray(moveArray);
    }
    
    // Pijltjes laten bewegen
    // Events aan 'Enter' gelinkt: stenen oppakken en plaatsen
    public void userEventReceived(UserEvent e) {
       if (e.getType()==HRcEvent.KEY_PRESSED && helloTvXlet.getGameOver()==false)
       {
           // Events gelinkt aan de pijltjes als ze binnen het bord blijven
           if (e.getCode()==HRcEvent.VK_RIGHT && curx < 9) curx++;
           else if (e.getCode()==HRcEvent.VK_LEFT && curx > 0) curx--;
           else if (e.getCode()==HRcEvent.VK_DOWN && cury < 9) cury++;
           else if (e.getCode()==HRcEvent.VK_UP && cury > 0) cury--;
           
           // Wanneer er op 'Enter' wordt gedrukt en er is nog geen steen opgepakt
           else if(e.getCode()==HRcEvent.VK_ENTER && taken == false)
           {
               tookDam = false;
               if(turnPlayer==1) // Speler 1 aan beurt
               {// Als er op de huidige positie een steen kan worden opgepakt
                  if(player1Array[curx][cury]==1 || player1Array[curx][cury]==2)
                  {
                      canHit(player1Array, player2Array); // check of speler 1 kan slaan (verplicht!)
                      
                      if(canHit)// Als je kan slaan
                      { // Als de steen die je wil oppakken kan slaan, pak hem op
                          if(moveArray[curx][cury]==1)
                          {
                              if(player1Array[curx][cury]==2) tookDam = true; // dam steen opgepakt
                              player1Array[curx][cury] = 0;
                              taken = true;
                              takenX = curx;
                              takenY = cury;
                          }
                      }
                      else // Als je niet kan slaan mag je een steen naar keuze oppakken
                      {
                          if(player1Array[curx][cury]==2) tookDam = true; // dam steen opgepakt
                          player1Array[curx][cury] = 0;
                          taken = true;
                          takenX = curx;
                          takenY = cury;
                      }
                  }
               }
               else // Speler 2 aan beurt
               {
                    if(player2Array[curx][cury]==1 || player2Array[curx][cury]==2)
                    {
                        canHit(player2Array, player1Array); // check of speler 2 kan slaan (verplicht!)
                        
                        if(canHit) // Als je kan slaan
                        { // Als de steen die je wil oppakken kan slaan, pak hem op
                            if(moveArray[curx][cury]==1) // 
                            {
                                if(player2Array[curx][cury]==2) tookDam = true; // dam steen opgepakt
                                player2Array[curx][cury] = 0;
                                taken = true;
                                takenX = curx;
                                takenY = cury;
                            }
                        }
                        else // Als je niet kan slaan, pak een steen naar keuze op
                        {
                            if(player2Array[curx][cury]==2) tookDam = true; // dam steen opgepakt
                            player2Array[curx][cury] = 0;
                            taken = true;
                            takenX = curx;
                            takenY = cury;
                        }
                    }
               }
           } // Wanneer er op 'Enter' wordt gedrukt en er is al een steen opgepakt
           else if(e.getCode()==HRcEvent.VK_ENTER && taken==true)
           {
               if(turnPlayer==1)
               {
                   if(tookDam) // dam verplaatsen
                   {
                       moves(player1Array, player2Array,0); // verplaats dam
                       
                       if(hit)
                       {// aantal stenen die een dam per zet slaat
                           for(int i = 0; i<hitCounter; i++) 
                           {
                               helloTvXlet.addScorePlayer1();
                               //if(helloTvXlet.score2==0) helloTvXlet.winner("Speler 1");
                           }
                           hit = false;
                       }
                   }
                   else // normale steen verplaatsen
                   {
                       moves(player1Array, player2Array, 1); // verplaats steen

                       if(cury == 9) // Aan de achterkant van het veld. Steen wordt dam.
                       {
                           helloTvXlet.changeMessage("Speler 1 heeft een dam!");
                           player1Array[curx][cury] = 2;
                       }

                       if(hit)
                       { // Score player 1 verhogen
                           helloTvXlet.addScorePlayer1();
                           hit = false;
                           //if(helloTvXlet.score2==0) helloTvXlet.winner("Speler 1");
                       }
                   }
               }
               else if(turnPlayer==2)
               {
                   if(tookDam) // dam verplaatsen
                   {
                       moves(player2Array, player1Array,0); // verplaats dam
                       
                       if(hit)
                       {
                           for(int i = 0; i<hitCounter; i++)
                           {
                               helloTvXlet.addScorePlayer2();
                               //if(helloTvXlet.score1==0) helloTvXlet.winner("Speler 2");
                           }
                           hit = false;
                       }
                   }
                   else // normale steen verplaatsen
                   {
                       moves(player2Array, player1Array, -1); // verplaats steen

                       if(hit)
                       { // Score player 2 verhogen
                           helloTvXlet.addScorePlayer2();
                           hit = false;
                           //if(helloTvXlet.score1==0) helloTvXlet.winner("Speler 2");
                       }
                       if(cury==0)
                       {
                           helloTvXlet.changeMessage("Speler 2 heeft een dam!");
                           player2Array[curx][cury] = 2;
                       }
                   } 
               }
           }
           this.repaint();
       }
    }
    
    // Gaat alle stenen in de array van de speler af en checkt of er een steen kan slaan
    // Past 'canHit' boolean aan
    // canHit true: speler kan een steen van de tegenstander slaan
    // canHit false: speler kan niet slaan
    public void canHit(int[][] ownArray, int[][]opponentArray)
    {
        cleanArray(moveArray);
        canHit = false;
        boolean ownBlock = false; // als je door eigen steen geblokkeerd wordt
        for(int x = 0; x < ownArray.length; x++)
        {
            for(int y = 0; y < ownArray.length; y++)
            {
                if(ownArray[x][y]==2) // dam kan slaan
                {
                    // links boven
                    if(x>1 && y>1) // binnen het bord blijven
                    {
                        int i,j;
                        for(i=x-1,j=y-1;i>=1 && j>=1;i--,j--) // kan op heel de diagonaal slaan
                        {
                            if(ownArray[i][j]!=0) ownBlock = true;
                            // Checken of er een steen staat van de tegenstander
                            if(opponentArray[i][j]!=0 && ownBlock == false)
                            {
                                // Checken of achter die steen plaats vrij is
                                if(freeSpaceBehind(i,j,-1,-1,ownArray,opponentArray))
                                {
                                    canHit = true;
                                    moveArray[x][y] = 1;
                                }
                            }
                        }
                    }
                    // rechts boven
                    if(x<8 && y>1)
                    {
                        ownBlock = false;
                        int i,j;
                        for(i=x+1,j=y-1;i<=8 && j>=1;i++,j--) // kan op heel de diagonaal slaan
                        {
                            if(ownArray[i][j]!=0) ownBlock = true;
                            if(opponentArray[i][j]!=0 && ownBlock == false)
                            {
                                // Checken of achter die steen plaats vrij is
                                if(freeSpaceBehind(i,j,1,-1,ownArray,opponentArray))
                                {
                                    canHit = true;
                                    moveArray[x][y] = 1;
                                }
                            }
                        }
                    }
                    // rechts onder
                    if(x<8 && y<8)
                    { 
                        ownBlock = false;
                        int i,j;
                        for(i=x+1,j=y+1;i<=8 && j<=8;i++,j++) // kan op heel de diagonaal slaan
                        {
                            if(ownArray[i][j]!=0) ownBlock = true;
                            if(opponentArray[i][j]!=0 && ownBlock == false)
                            {
                                // Checken of achter die steen plaats vrij is
                                if(freeSpaceBehind(i,j,1,1,ownArray,opponentArray))
                                {
                                    canHit = true;
                                    moveArray[x][y] = 1;
                                }
                            }
                        }
                    }
                    // links onder
                    if(x>1 && y<8)
                    {
                        ownBlock = false;
                        int i,j;
                        for(i=x-1,j=y+1;i>=1 && j<=8;i--,j++) // kan op heel de diagonaal slaan
                        {
                            if(ownArray[i][j]!=0) ownBlock = true;
                            if(opponentArray[i][j]!=0 && ownBlock == false)
                            {
                                // Checken of achter die steen plaats vrij is
                                if(freeSpaceBehind(i,j,-1,1,ownArray,opponentArray))
                                {
                                    canHit = true;
                                    moveArray[x][y] = 1;
                                }
                            }
                        }
                    }
                }
                if(ownArray[x][y]==1) // gewone steen kan slaan
                {
                    if(x>1 && y >1) // binnen het bord blijven
                    {
                        // Check of er een blokje van de tegenstander rondligt en daarachter een leeg vakje is
                        // Links boven
                        if(opponentArray[x-1][y-1]!=0 && ownArray[x-2][y-2]==0 && opponentArray[x-2][y-2]==0) // links boven
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                        }
                    }
                    if(x<8 && y>1)
                    {
                        // Rechts boven
                        if(opponentArray[x+1][y-1]!=0 && ownArray[x+2][y-2]==0 && opponentArray[x+2][y-2]==0) // rechts boven
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                        } 
                    }
                    if(x>1 && y<8)
                    {
                        // Links onder
                        if(opponentArray[x-1][y+1]!=0 && ownArray[x-2][y+2]==0 && opponentArray[x-2][y+2]==0) // links onder
                        {
                            // toevoegen aan array
                            moveArray[x][y]=1;
                            canHit = true;
                        }
                    }
                    if(x<8 && y<8)
                    {
                      // Rechts onder
                      if(opponentArray[x+1][y+1] !=0 && ownArray[x+2][y+2]==0 && opponentArray[x+2][y+2]==0) // rechts onder
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
    // offset voor player 1 = 1
    // offset voor player 2 = -1
    public void moves(int[][] ownArray, int[][] opponentArray, int offset)
    {
       // Je kan niet buiten het veld komen en de coordinaten moeten leeg zijn
       if(curx < 10 && curx >= 0 && cury < 10 && cury >=0 && player1Array[curx][cury]==0 && player2Array[curx][cury]==0)
       {
           if(tookDam==false) // gewone steen verplaatsen
           {
               if(canHit) // je moet slaan
               {
                   if(curx==takenX+2 && cury==takenY+2) // Rechts onder slaan
                   {
                       if(opponentArray[takenX+1][takenY+1]!=0) // Als je een stuk van de tegenstander kan slaan
                       {
                           opponentArray[takenX+1][takenY+1]=0; // Steen van de tegenstander van het bord halen
                           ownArray[curx][cury] = 1; // Je eigen steen plaatsen
                           taken = false;
                           hit = true;
                       }
                   }
                   else if(curx==takenX+2 && cury==takenY-2) // Rechts boven slaan
                   {
                       if(opponentArray[takenX+1][takenY-1]!=0) // Als je een stuk van de tegenstander kan slaan
                       {
                           opponentArray[takenX+1][takenY-1]=0; // Steen van de tegenstander van het bord halen
                           ownArray[curx][cury] = 1; // Eigen steen plaatsen
                           taken = false;
                           hit = true;
                       }
                   }
                   else if(curx==takenX-2 && cury==takenY+2) // Links onder slaan
                   {
                       if(opponentArray[takenX-1][takenY+1]!=0) // Als je een stuk van de tegenstander kan slaan
                       {
                           opponentArray[takenX-1][takenY+1]=0; // Steen van de tegenstander van het bord halen
                           ownArray[curx][cury] = 1; // Eigen steen plaatsen
                           taken = false;
                           hit = true;
                       }
                   }
                   else if(curx==takenX-2 && cury==takenY-2) // Links boven slaan
                   {
                       if(opponentArray[takenX-1][takenY-1]!=0) // Als je een stuk van de tegenstander kan slaan
                       {
                           opponentArray[takenX-1][takenY-1]=0; // Steen van de tegenstander van het bord halen
                           ownArray[curx][cury] = 1; // Eigen steen plaatsen
                           taken = false;
                           hit = true;
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
                       helloTvXlet.changeMessage("Je mag enkel op dezelfde plaats staan als je niet anders kan.");
                       if(freeSpaceAroundPiece(curx,cury)==false)
                       {
                           ownArray[curx][cury] = 1;
                           taken = false;
                       }
                   }
               } 
           }
           else if(tookDam==true) // met een dam slaan
           {
               if(possibleMoves(ownArray,opponentArray, takenX, takenY)) // mogelijke plaatsen waar de dam kan staan
               {
                   if(canHit) // slaan is verplicht
                   { // als de gekozen plaats toegankelijk is voor de dam
                        if(possibleMoves[curx][cury]==1) 
                        { // als er een steen van de tegenstander geslagen kan worden
                            if(opponentBetween(takenX,takenY,curx,cury,ownArray,opponentArray))
                            {
                                ownArray[curx][cury]=2;
                                taken = false;
                                hit = true;
                                
                                // Als de speler niet meer verder kan slaan is de beurt aan de tegenstander
                                canHit(ownArray, opponentArray);
                                if(canHit == false &&  hit==true)
                                {
                                    switchPlayer();
                                }
                            }
                        }
                   }
                   else // gewone stap/sprong zetten
                   {
                        if(possibleMoves[curx][cury]==1) // als de dam op de gekozen plaats kan staan
                        {
                            ownArray[curx][cury]=2;
                            taken = false;
                            switchPlayer();
                        }

                   }
               }
               else if(curx == takenX && cury==takenY) // zelfde plaats terugzetten als er geen andere optie is
               {
                    ownArray[curx][cury]=2;
                    taken = false;
               }
           }
        }
    }
     
    // Kijkt welke stenen er allemaal op de diagonaal van een bepaalde dam liggen
    // En stopt ze in de array possibleMoves
    // Als ze een steen van hun eigen array stopt de zoektocht op die diagonaal
    // coorX, coorY is de steen waarbij de functie moet checken welke posities er mogelijk zijn
    public boolean possibleMoves(int[][] ownArray, int[][]opponentArray, int coorX, int coorY)
    {
        cleanArray(possibleMoves);
        boolean canMove = false; // true: steen kan niet verplaatst worden; false: steen kan verplaatst worden
        boolean ownBlock = false; // true: als je een steen van je zelf tegenkomt op de diagonaal

        // links boven
        int i,j;
        for(i=coorX-1,j=coorY-1;i>=0 && j>=0;i--,j--) // heel de diagonaal afgaan
        {
            if(ownArray[i][j]!=0) ownBlock=true; // steen van jezelf tegengekomen
            if(opponentArray[i][j]==0 && ownBlock==false) // lege positie
            {
                possibleMoves[i][j]=1;
                canMove = true;
            }
        }
        
        // rechts boven
        ownBlock = false; // reset voor nieuwe diagonaal
        for(i=takenX+1,j=takenY-1;i<10 && j>=0;i++,j--) // heel de diagonaal afgaan
        {
            if(ownArray[i][j]!=0) ownBlock=true; // steen van jezelf tegengekomen
            if(opponentArray[i][j]==0 && ownBlock==false) // lege positie
            {
                possibleMoves[i][j]=1;
                canMove = true;
            }
        }
        
        // rechts onder
        ownBlock = false; // reset
        for(i=takenX+1,j=takenY+1;i<10 && j<10;i++,j++) // heel de diagonaal afgaan
        {
            if(ownArray[i][j]!=0) ownBlock=true;
            if(opponentArray[i][j]==0 && ownBlock==false)
            {
                possibleMoves[i][j]=1;
                canMove = true;
            }
        }
        
        // links onder
        ownBlock = false; // reset
        for(i=takenX-1,j=takenY+1;i>=0 && j<10;i--,j++) // heel de diagonaal afgaan
        {
            if(ownArray[i][j]!=0) ownBlock=true;
            if(opponentArray[i][j]==0 && ownBlock==false)
            {
                possibleMoves[i][j]=1;
                canMove = true;
            }
        }
        return canMove;
    }
    
    // prevX, prevY is waar de steen vandaan komt (waar opgepakt)
    // newX, newY is waar de speler de steen wil neerzetten
    // Tussenliggende stenen van de tegenstander worden geslagen
    // Return true: stenen van de tegenstander zijn geslagen
    public boolean opponentBetween(int prevX, int prevY, int newX, int newY, int[][] ownArray, int[][] opponentArray)
    {
        // if Hit = true: staat een speler tussen de gegeven coordinaten, deze is geslagen
        // if Hit = false: staat niemand tussen de coordinaten
        boolean Hit = false;
        hitCounter = 0;
        
        if(prevX > newX && prevY > newY) // links boven
        {
            int i,j;
            for(i=prevX,j=prevY;i>newX && j>newY;i--,j--)
            {
                if(opponentArray[i][j]!=0)
                {
                    opponentArray[i][j]=0;
                    hitCounter++;
                    Hit=true;
                }
            }
        }
        else if(prevX > newX && prevY < newY) // links onder
        {
            int i,j;
            for(i=prevX,j=prevY;i>newX && j<newY;i--,j++)
            {
                if(opponentArray[i][j]!=0)
                {
                    opponentArray[i][j]=0;
                    hitCounter++;
                    Hit=true;
                }
            }
        }
        else if(prevX < newX && prevY < newY) // rechts onder
        {
            int i,j;
            for(i=prevX,j=prevY;i<newX && j<newY;i++,j++)
            {
                if(opponentArray[i][j]!=0)
                {
                    opponentArray[i][j]=0;
                    hitCounter++;
                    Hit=true;
                }
            }
        }
        else if(prevX < newX && prevY > newY) // rechts boven
        {
            int i,j;
            for(i=prevX,j=prevY;i<newX && j>newY;i++,j--)
            {
                if(opponentArray[i][j]!=0)
                {
                    opponentArray[i][j]=0;
                    hitCounter++;
                    Hit=true;
                }
            }
        }
        return Hit;
    }
    
    // Links onder: offsetX == -1 && offsetY == 1
    // Rechts onder: offsetX == 1 && offsetY == 1
    // Rechts boven: offsetX == 1 && offsetY == -1
    // Links boven: offsetX == -1 && offsetY == -1
    // Return true: als er vrije plaatsen zijn achter de meegegeven coordinaten/steen
    // Als er een eigen steen tegengekomen wordt kan je hier achter geen steen meer zetten
    public boolean freeSpaceBehind(int coorX, int coorY, int offsetX, int offsetY, int[][] ownArray, int[][] opponentArray)
    {
        boolean freeSpace = false;
        boolean ownBlock = false; // als je een eigen steen tegenkomt stopt je met zoeken in die richting
        int i,j;
 
        // links onder
        if(offsetX == -1 && offsetY==1)
        {
             for(i=coorX+offsetX, j=coorY+offsetY; i>=0 && j<10 && ownArray[i][j] == 0; i--, j++)
             {
                if(ownArray[i][j]!=0) ownBlock = true;
                if(opponentArray[i][j]==0 && ownBlock == false)
                {
                    //possibleMoves[i][j]=1;
                    freeSpace = true;
                }
             }
        }
        // rechts onder
        else if(offsetX ==1 && offsetY==1)
        {
            ownBlock = false;
            for(i=coorX+offsetX, j=coorY+offsetY; i<10 && j<10 && ownArray[i][j] == 0; i++, j++)
            {
                if(ownArray[i][j]!=0) ownBlock = true;
                if(opponentArray[i][j]==0 && ownBlock == false)
                {
                    //possibleMoves[i][j]=1;
                    freeSpace = true;
                }
            }
        }
        
        // rechts boven
        else if(offsetX ==1 && offsetY==-1)
        {
            ownBlock = false;
            for(i=coorX+offsetX, j=coorY+offsetY; i<10 && j>=0 && ownArray[i][j] == 0; i++, j--)
            {
                if(ownArray[i][j]!=0) ownBlock = true;
                if(opponentArray[i][j]==0 && ownBlock == false)
                {
                    //possibleMoves[i][j]=1;
                    freeSpace = true;
                }
            }
        }
        
        // links boven
        else if(offsetX ==-1 && offsetY==-1)
        {
            ownBlock = false;
            for(i=coorX+offsetX, j=coorY+offsetY; i>=0 && j>=0 && ownArray[i][j] == 0; i--, j--)
            {
                if(ownArray[i][j]!=0) ownBlock = true;
                if(opponentArray[i][j]==0 && ownBlock == false)
                {
                    //possibleMoves[i][j]=1;
                    freeSpace = true;
                }
            }
        }
        
        return freeSpace;
    }

    // Alle stenen in de array worden gelijkgesteld aan 0
    public void cleanArray(int[][] arr)
    {
       for(int x=0; x < arr.length; x++)
        {
            for(int y = 0; y < arr.length; y++)
            {
                arr[x][y] = 0;
            }
        } 
    }
    
    // Andere speler is aan de beurt
    public void switchPlayer()
    {
      if(turnPlayer == 1){
        turnPlayer = 2;
        helloTvXlet.changeMessage("Speler 2 is aan zet");
      }
        
      else{
        turnPlayer = 1;
        helloTvXlet.changeMessage("Speler 1 is aan zet.");
      }
    }
    
    // Kijkt of de vakjes rond de meegegeven coordinaten leeg zijn
    // Als offset 1 is dan 
    // Return true --> er zijn nog plaatsen waar de damsteen kan staan
    // Return false --> de damsteen kan nergens staan
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
