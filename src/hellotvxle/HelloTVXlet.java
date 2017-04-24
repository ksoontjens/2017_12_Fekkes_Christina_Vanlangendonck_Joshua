package hellotvxle;

import java.awt.event.ActionEvent;
/*import java.awt.event.ActionEvent;*/import javax.tv.xlet.*;
import org.dvb.event.EventManager;
import org.dvb.event.UserEventRepository;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.havi.ui.event.HActionListener;


public class HelloTVXlet implements Xlet, HActionListener {

    HScene scene;
    public HelloTVXlet() {
        
    }

    public void initXlet(XletContext context) { //720 x 576 (make smaller later)
      scene=HSceneFactory.getInstance().getDefaultHScene();
      ChessBoard bord=new ChessBoard();
         UserEventRepository repo=new UserEventRepository("repo");
         repo.addAllArrowKeys();
     EventManager man=EventManager.getInstance();
     man.addUserEventListener(bord, repo);
     
      scene.add(bord);
      scene.validate();
      scene.setVisible(true);
    }

    public void startXlet() {
  
    }

    public void pauseXlet() {
     
    }

    public void destroyXlet(boolean unconditional) {
     
    }

    public void actionPerformed(ActionEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    }
