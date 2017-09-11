

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClientin extends Thread{
	Socket socketClient;
	TankClient tc;
	int player;
	String address;
	String nickname;
	TCPClientin(TankClient tc, int player, String address, String nickname) {
		this.tc = tc;
		this.player = player;
		this.address = address;
		this.nickname = nickname;
	}
	public void run() {
		try {
			System.out.println("connect");
			socketClient = new Socket(this.address, 15723);
			DataOutputStream oos = new DataOutputStream(socketClient.getOutputStream());

			
			oos.writeInt(-this.player);
			oos.flush();
			oos.writeUTF(nickname);
			oos.flush();
//			ObjectInputStream ois = new ObjectInputStream(socketClient.getInputStream());
			DataInputStream ois = new DataInputStream(socketClient.getInputStream());
			while (true) {
				System.out.println("has read");
				//states = (States)ois.readObject();
				tc.jsstring = ois.readUTF();
				System.out.println(tc.jsstring);
			}
		} catch (Exception e) {}
		
	}
}
