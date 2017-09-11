

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClientout extends Thread{
	Socket socketClient;
	int keynum;
	String address;
	TCPClientout(int keynum, String address) {
		this.keynum = keynum;
		this.address = address;
	}
	public void run() {
		try {
			socketClient = new Socket(this.address, 15723);
			DataOutputStream oos = new DataOutputStream(socketClient.getOutputStream());
			oos.writeInt(keynum);
			oos.flush();
			oos.close();
			socketClient.close();
		} catch (Exception e) {}
		
	}
}