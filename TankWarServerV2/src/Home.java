import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Home {
	private int x, y;
	private TankServer tc;
	public static final int width = 30, length = 30; // ȫ�־�̬��������
	boolean live = true;

	private static Toolkit tk = Toolkit.getDefaultToolkit(); // ȫ�־�̬����
	private static Image[] homeImags = null;
	static {
		homeImags = new Image[] { tk.getImage(CommonWall.class.getResource("Images/home.jpg")), };
	}

	public Home(int x, int y, TankServer tc) {// ���캯��������Home�Ĳ�������ֵ
		this.x = x;
		this.y = y;
		this.tc = tc; // ��ÿ���
	}

	public void gameOver(Graphics g) {

		tc.tanks.clear();// ������ҳ�湤��
		tc.metalWall.clear();
		tc.otherWall.clear();
		tc.bombTanks.clear();
		tc.theRiver.clear();
		tc.trees.clear();
		tc.bullets.clear();
		tc.homeTank.setLive(false);
		Color c = g.getColor(); // ���ò���
		g.setColor(Color.green);
		Font f = g.getFont();
		g.setFont(new Font("TimesRoman", Font.BOLD, 40));
		g.drawString("�����ˣ�", 100, 250);
		g.drawString("  ��Ϸ������ ", 100, 300);
		if (!this.tc.iswrittern) {
			String[] stmp = {this.tc.player1nickname,this.tc.player2nickname};
			int[] itmp = {tc.homeTank.score, tc.homeTank2.score};
			aboutFile af1 = new aboutFile();
			af1.run(g, stmp, itmp);
			this.tc.top5s = af1.top5s;
			this.tc.top5i = af1.top5i;
			this.tc.iswrittern = true;
		} else {
			new aboutFile().showprint(g);
		}

	}

	public void draw(Graphics g) {

		if (live) { // ������ţ��򻭳�home
			g.drawImage(homeImags[0], x, y, null);

			for (int i = 0; i < tc.homeWall.size(); i++) {
				CommonWall w = tc.homeWall.get(i);
				w.draw(g);
			}
		} else {
			gameOver(g); // ������Ϸ����

		}
	}

	public boolean isLive() { // �ж��Ƿ񻹻���
		return live;
	}

	public void setLive(boolean live) { // ��������
		this.live = live;
	}

	public Rectangle getRect() { // ���س�����ʵ��
		return new Rectangle(x, y, width, length);
	}

}
