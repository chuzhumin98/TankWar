import java.awt.BorderLayout;
import java.awt.Font;
import java.io.*;
import java.net.*;

import javax.swing.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
public class TCPMultiServer {
    static int nClientNum = 0;
    public static void main(String[] args) throws Exception{
    	TankServer tc = new TankServer();
    	Num ntmp = new Num();
    	localip lip1 = new localip();
    	lip1.getV4IP();
        JFrame frame = new JFrame();
        JLabel jl1 = new JLabel();
    	frame.setTitle("server");
        frame.setSize(370, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(jl1,BorderLayout.CENTER);     
        String stmp = InetAddress.getLocalHost().getHostAddress();
        String stmp1 = localip.getV4IP();
        jl1.setFont(new Font("Dialog", Font.PLAIN, 18));
        jl1.setText("HostAddress:"+stmp1);
        frame.setVisible(true);
        System.out.println(InetAddress.getLocalHost().getHostName());
        System.out.println(InetAddress.getLocalHost());
		ServerSocket ssocketWelcome = new ServerSocket(15723);
        System.out.println("Start to work");
        while(true){
            Socket socketServer = ssocketWelcome.accept();
            nClientNum ++;
            TCPServerThread thread;
            System.out.println(nClientNum);
            thread = new TCPServerThread(socketServer, nClientNum, tc, ntmp);
            thread.start();
        }
    }    
}
class localip {
	public static String getV4IP(){
        try {
            URL u = new URL("http://members.3322.org/dyndns/getip");
            InputStream in = u.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String theLine;
            while ((theLine = br.readLine()) != null) {
                System.out.println(theLine);
                return theLine;
            }
            return theLine;
        } catch (Exception e) {return null;}
		
	}
}