import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class bgMusic implements Runnable{
 
    public void play(String path) {
        try {
        	String pui = BombTank.class.getClassLoader().getResource("Images/bg1.mp3").getPath();
        	System.out.println(pui);
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(pui));
            player = new Player(buffer);
            player.play();
            new Thread(new bgMusic()).start();
        } catch (Exception e) {
            System.out.println(e);
        }
 
    }
 
    public void run() {
    	String path =Thread.currentThread().getContextClassLoader().getResource("").getPath();
		System.out.println(path);
		play(path);
    }
 
    private String filename;
    private Player player;
 
    public static void main(String[] args) {
    	new Thread(new bgMusic()).start();
    }
}
