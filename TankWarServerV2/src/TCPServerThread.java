import java.io.*;
import java.net.*;


public class TCPServerThread extends Thread { //数据输出的类
    Socket socket;
    int nClientNum;
    int keynum; //表征谁下的位置（id*10+位置）
    TankServer tc;
    Num num;
    public TCPServerThread(Socket s, int num, TankServer tc, Num n){
        this.socket = s;
        this.nClientNum = num;
        this.tc = tc;
        this.keynum = 0;
        this.num = n;
    }
    public void run() {
        try{
           
            DataInputStream  dis = 
                    new DataInputStream(socket.getInputStream());
            keynum = dis.readInt();  
            System.out.println("read keynum "+ this.keynum);
            if (keynum == -1) { //用户1的讯号
            	TCPServerout t1 = new TCPServerout(this.socket, this.nClientNum, this.tc);
            	this.tc.player1nickname = dis.readUTF(); //读取昵称
            	//this.tc.printable = true;
            	System.out.println("player1:"+this.tc.player1nickname);
            	t1.run();
            }
            if (keynum == -2) { //用户2的讯号
            	
            	TCPServerout t2 = new TCPServerout(this.socket, this.nClientNum, this.tc);
            	this.tc.player2nickname = dis.readUTF(); //读取昵称
            	//this.tc.printable = true;
            	System.out.println("player2:"+this.tc.player2nickname);
            	t2.run();
            }
           // if (count == 1) {
            	
           // }
            if (keynum/10 == 21) {
            	tc.homeTank.keyReleasedforserver(keynum);
            }
            if (keynum/10 == 11) {
            	tc.homeTank.keyPressedforserver(keynum);
            }
            if (keynum/10 == 22) {
            	tc.homeTank2.keyReleased2forserver(keynum);
            }
            if (keynum/10 == 12) {
            	tc.homeTank2.keyPressed2forserver(keynum);
            }
            if (keynum == 998) {
            	this.num.count++;
            	this.num.player1ok = true;
            	if (this.num.player1ok && this.num.player2ok) {
            		tc.started();
            	}
            }
            if (keynum == 999) {
            	this.num.count++;
            	this.num.player2ok = true;
            	if (this.num.player1ok && this.num.player2ok) {
            		tc.started();
            	}
            }
            if (keynum == 1000) {
            	tc.printable = false;
            }
            if (keynum == 1001) {
            	tc.started();
            }
            if (keynum == 1002) {
            	tc.init();
            }
            if (keynum == 1011) {           	
            	Tank.count = 12;
    			Tank.speedX = 6;
    			Tank.speedY = 6;
    			Bullets.speedX = 10;
    			Bullets.speedY = 10;
    			tc.init();
            }
            if (keynum == 1012) {
            	Tank.count = 12;
    			Tank.speedX = 10;
    			Tank.speedY = 10;
    			Bullets.speedX = 12;
    			Bullets.speedY = 12;
    			tc.init();
            }
            if (keynum == 1013) {
            	Tank.count = 20;
    			Tank.speedX = 14;
    			Tank.speedY = 14;
    			Bullets.speedX = 16;
    			Bullets.speedY = 16;
    			tc.init();
            }
            if (keynum == 1014) {
            	Tank.count = 20;
    			Tank.speedX = 16;
    			Tank.speedY = 16;
    			Bullets.speedX = 18;
    			Bullets.speedY = 18;
    			tc.init();
            }
             dis.close();
            socket.close();
         }catch(Exception e){}
     }   
 }