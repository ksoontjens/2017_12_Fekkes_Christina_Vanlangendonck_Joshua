package hellotvxlet;

import java.awt.event.ActionEvent;
/*import java.awt.event.ActionEvent;*/import javax.tv.xlet.*;
import org.dvb.event.EventManager;
import org.dvb.event.UserEventRepository;
import org.dvb.ui.DVBColor;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.havi.ui.HStaticText;
import org.havi.ui.HComponent;
import org.havi.ui.event.HActionListener;
import org.havi.ui.HVisible;


public class HelloTVXlet extends HComponent implements Xlet, HActionListener {

    HScene scene;
   
    static public HStaticText gameMessage;
    static public HStaticText scorePlayer1;
    static public HStaticText scorePlayer2;
    static private HStaticText winnerScreen;
    private HStaticText labelPlayer1;
    private HStaticText labelPlayer2;
    private boolean gameover = false;
    
    public int score1 = 20; // aantal stenen op het veld
    public int score2 = 20;

    public HelloTVXlet() {
        
    }

    public void initXlet(XletContext context) { //720 x 576
      scene=HSceneFactory.getInstance().getDefaultHScene();
      ChessBoard bord=new ChessBoard();
         UserEventRepository repo=new UserEventRepository("repo");
         repo.addAllArrowKeys();
         repo.addAllNumericKeys();
         repo.addKey( org.havi.ui.event.HRcEvent.VK_ENTER );
     EventManager man=EventManager.getInstance();
     man.addUserEventListener(bord, repo);
     
     
      labelPlayer1 = new HStaticText("Speler 1:");
      labelPlayer1.setLocation(585, 100);
      labelPlayer1.setSize(100,40);
      
      labelPlayer2 = new HStaticText("Speler 2:");
      labelPlayer2.setLocation(585, 400);
      labelPlayer2.setSize(100,40);
      
      scorePlayer1 = new HStaticText(Integer.toString(score1));
      scorePlayer1.setLocation(585,140);
      scorePlayer1.setSize(100,40);
      
      scorePlayer2 = new HStaticText(Integer.toString(score2));
      scorePlayer2.setLocation(585,440);
      scorePlayer2.setSize(100,40);
     
      gameMessage = new HStaticText("Welkom! Speler 1 mag het spel beginnen.");
      gameMessage.setLocation(50,5);
      gameMessage.setSize(650,40);
      gameMessage.setBackground(new DVBColor(1,255,1,100));
      gameMessage.setBackgroundMode(HVisible.BACKGROUND_FILL);
      
      winnerScreen = new HStaticText("");
      winnerScreen.setLocation(50,50);
      winnerScreen.setSize(500,500);
      winnerScreen.setBackground(new DVBColor(1,100,1,200));
      winnerScreen.setBackgroundMode(HVisible.NO_BACKGROUND_FILL);
      
 
      scene.add(winnerScreen);
      scene.add(gameMessage);
      scene.add(scorePlayer1);
      scene.add(scorePlayer2);
      scene.add(labelPlayer1);
      scene.add(labelPlayer2);
      scene.add(bord);
      scene.validate();
      scene.setVisible(true);
    }

    public void startXlet() {
       
    }

    public void pauseXlet() {
     
    }
    
    public boolean getGameOver()
    {
        return gameover;
    }
    
    public void winner(java.lang.String winner)
    {
        winnerScreen.setTextContent(winner + " heeft gewonnen!", HVisible.NORMAL_STATE);
        winnerScreen.setBackgroundMode(HVisible.BACKGROUND_FILL);
        gameMessage.setTextContent("Proficiat!", HVisible.NORMAL_STATE);
        gameover = true;
    }
    
    public void addScorePlayer1() {
        score2--;
        scorePlayer2.setTextContent(Integer.toString(score2),HVisible.NORMAL_STATE);
        if(score2==0) winner("Speler 1");
    }
    
    public void addScorePlayer2() {
        score1--;
        scorePlayer1.setTextContent(Integer.toString(score1),HVisible.NORMAL_STATE);
        if(score1==0) winner("Speler 2");
    }
    
    public void changeMessage(java.lang.String str)
    {
        gameMessage.setTextContent(str,HVisible.NORMAL_STATE);
    }

    public void destroyXlet(boolean unconditional) {
     
    }

    public void actionPerformed(ActionEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    }
