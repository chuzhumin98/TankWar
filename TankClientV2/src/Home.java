import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Home {
	private int x, y;
	private TankClient tc;
	public static final int width = 30, length = 30; // ȫ�־�̬��������
	boolean live = true;

	private static Toolkit tk = Toolkit.getDefaultToolkit(); // ȫ�־�̬����
	private static Image[] homeImags = null;
	static {
		homeImags = new Image[] { tk.getImage(CommonWall.class
				.getResource("Images/home.jpg")), };
	}

	public Home(int x, int y, TankClient tc) {// ���캯��������Home�Ĳ�������ֵ
		this.x = x;
		this.y = y;
		this.tc = tc; // ��ÿ���
	}

	public void gameOver(Graphics g, JSONObject jso1) {

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
		g.setFont(new Font(" ", Font.PLAIN, 40));
		g.drawString("�����ˣ�", 100, 250);
		g.drawString("  ��Ϸ������ ", 100, 300);
		g.setFont(f);
		g.setColor(c);
		
		g.setColor(Color.green); // ����������ʾ����
		g.setFont(new Font("TimesRoman", Font.BOLD, 35)); 
		g.drawString("Top 5 ", 350, 200);
		g.setFont(new Font("TimesRoman", Font.BOLD, 25));
		if (jso1.get("top5s") != null) {
			this.tc.top5s = JSONArray.fromObject(jso1.get("top5s"));
			this.tc.top5i = JSONArray.fromObject(jso1.get("top5i"));
		}
		for (int i = 0; i < 5; i++) {
			g.drawString(tc.top5s.getString(i), 300, 260+i*30);
			g.drawString(tc.top5i.getString(i), 395, 260+i*30);
		}
		System.out.println("top5:"+tc.top5s.size());

	}

	public void draw(Graphics g, JSONObject jso1) {

		if (live) { // ������ţ��򻭳�home
			g.drawImage(homeImags[0], x, y, null);

			for (int i = 0; i < tc.homeWall.size(); i++) {
				CommonWall w = tc.homeWall.get(i);
				w.draw(g);
			}
		} else {
			gameOver(g, jso1); // �Ѿ�������-1�����ˣ����Բ����ظ�����

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
