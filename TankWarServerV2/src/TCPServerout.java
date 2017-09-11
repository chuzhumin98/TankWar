import java.io.*;
import java.net.*;
public class TCPServerout{ //数据输出的类
    Socket socket;
    int nClientNum;
    TankServer tc;
    int curVersion; //当前版本
    public TCPServerout(Socket s, int num, TankServer tc){
        this.socket = s;
        this.nClientNum = num;
        this.tc = tc;
        this.curVersion = 0;
    }
    public void run() {
        try{
        	System.out.println("player x ok");
        	//ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        	DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            System.out.println("write to player x create"); 
            while (true) {
            	Thread.sleep(100);
            	oos.writeUTF(this.tc.TransiJSON());
            	oos.flush();
            	//System.out.println("send all");
            }
            //socket.close();
         }catch(Exception e){}
     }   
 }