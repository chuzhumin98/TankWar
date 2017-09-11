import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class bgMusic2V2 implements Runnable{ 
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {      
				URL u1 = BombTank.class.getClassLoader().getResource("Images/bg2.wav");
		       AudioClip aau = Applet.newAudioClip(u1);
		       aau.play();  //Ñ­»·²¥·Å
		       Thread.sleep(1000);
		       //new Thread(new bgMusic2V2()).start();
				} catch (Exception e) 
				{ e.printStackTrace();
				} 
		}
}