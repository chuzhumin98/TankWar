import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartFrame implements MouseListener{
	JFrame startframe = new JFrame();
	JTextField jt1,jt2;
	JButton jb1;
	JLabel jl1,jl2;
	JPanel jp1,jp2;
	String address;
	String nickname;
	boolean isstarted;
	StartFrame() {

		startframe.setTitle("player starting");
        startframe.setSize(370, 370);
        
        startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jp1 = new JPanel();
        jp2 = new JPanel();
        jl1 = new JLabel("Server address:");
        jt1 = new JTextField(10);
        jl2 = new JLabel("Nickname:");
        jt2 = new JTextField(10);
        

        jb1 = new JButton("Submit");
        startframe.setLayout(new FlowLayout());
        jp1.add(jl1);
        jp1.add(jt1);
        jp2.add(jl2);
        jp2.add(jt2);
        startframe.add(jp1);
        startframe.add(jp2);
        startframe.getContentPane().add(jb1);
        jb1.addMouseListener(this);
        startframe.setVisible(true);
	}
	boolean hastext(String a) {
		if (a == null) return false;
		for (int i = 0; i < a.length(); i++) {
			if (a.indexOf(i) != ' ') {
				return true;
			}
		}
		return false;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jb1) {
			String address = jt1.getText();
			String nickname = jt2.getText();
			if (hastext(address) && hastext(nickname) ) {
				this.address = address;
				this.nickname = nickname;
				startframe.dispose();
				this.isstarted = true;
			} else {
				JOptionPane.showMessageDialog(null, "服务器地址栏和昵称名不能为空",
						"提示！", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new StartFrame();
	}
}
