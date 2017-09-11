import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class bgMusicV2 implements Runnable{ 
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {      
				URL u1 = BombTank.class.getClassLoader().getResource("Images/bg1.wav");
		       AudioClip aau = Applet.newAudioClip(u1);
		       aau.play();  //Ñ­»·²¥·Å
		       Thread.sleep(111000);
		       new Thread(new bgMusicV2()).start();
				} catch (Exception e) 
				{ e.printStackTrace();
				} 
		}
}