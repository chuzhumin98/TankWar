import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bullets {
	public static  int speedX = 10;
	public static  int speedY = 10; // �ӵ���ȫ�־�̬�ٶ�

	public static final int width = 10;
	public static final int length = 10;

	int x, y;
	Direction diretion;

	private boolean good;
	private boolean live = true;

	private TankServer tc;
	Tank t;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] bulletImages = null;
	private static Map<Integer, Image> imgs = new HashMap<Integer, Image>(); // ����Map��ֵ�ԣ��ǲ�ͬ�����Ӧ��ͬ�ĵ�ͷ

	static {
		bulletImages = new Image[] { // ��ͬ������ӵ�
				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletL.gif")),

				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletU.gif")),

				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletR.gif")),

				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletD.gif")),

		};

		imgs.put(0, bulletImages[0]); // ����Map����

		imgs.put(1, bulletImages[1]);

		imgs.put(2, bulletImages[2]);

		imgs.put(3, bulletImages[3]);

	}

	public Bullets(int x, int y, Direction dir) { // ���캯��1������λ�úͷ���
		this.x = x;
		this.y = y;
		this.diretion = dir;
	}

	// ���캯��2������������������
	public Bullets(int x, int y, boolean good, Direction dir, TankServer tc, Tank t) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
		this.t = t;
	}

	private void move() {

		switch (diretion) {
		case L:
			x -= speedX; // �ӵ������������
			break;

		case U:
			y -= speedY;
			break;

		case R:
			x += speedX; // �ֶβ�������
			break;

		case D:
			y += speedY;
			break;

		case STOP:
			break;
		}

		if (x < 0 || y < 0 || x > TankServer.Fram_width
				|| y > TankServer.Fram_length) {
			live = false;
		}
	}

	public void draw(Graphics g) {
		if (!live) {
			tc.bullets.remove(this);
			return;
		}

		switch (diretion) { // ѡ��ͬ������ӵ�
		case L:
			g.drawImage(imgs.get(0), x, y, null);
			break;

		case U:
			g.drawImage(imgs.get(1), x, y, null);
			break;

		case R:
			g.drawImage(imgs.get(2), x, y, null);
			break;

		case D:
			g.drawImage(imgs.get(3), x, y, null);
			break;

		}

		move(); // �����ӵ�move()����
	}

	public boolean isLive() { // �ж��Ƿ񻹻���
		return live;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean hitTanks(List<Tank> tanks) {// ���ӵ���̹��ʱ
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) { // ��ÿһ��̹�ˣ�����hitTank
				return true;
			}
		}
		return false;
	}

	public boolean hitTank(Tank t) { // ���ӵ���̹����

		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive()
				&& this.good != t.isGood()) {

			BombTank e = new BombTank(t.getX(), t.getY(), tc);
			BombTank ecopy = new BombTank(t.getX(), t.getY(), tc);
			tc.bombfortransi.add(ecopy);
			tc.bombTanks.add(e);
			if (t.isGood()) {
				t.setLife(t.getLife() - 50); // ��һ���ӵ���������50������4ǹ����,������ֵ200
				if (t.getLife() <= 0)
					t.setLive(false); // ������Ϊ0ʱ����������Ϊ����״̬
			} else {
				t.setLife(t.getLife() - 50); // ��һ���ӵ���������50����̹ͨ��1ǹ�����߼�̹��2ǹ��
				if (t.getLife() <= 0) {
					if (t.ishigher) {
						this.t.score += 3; //����߼�̹�˼�3��
					} else {
						this.t.score += 1;
					}					
					t.setLive(false); 
				}
				
			}

			this.live = false;

			return true; // ����ɹ�������true
		}
		return false; // ���򷵻�false
	}

	public boolean hitWall(CommonWall w) { // �ӵ���CommonWall��
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			this.tc.otherWall.remove(w); // �ӵ���CommonWallǽ��ʱ���Ƴ��˻���ǽ
			this.tc.homeWall.remove(w);
			return true;
		}
		return false;
	}

	public boolean hitWall(MetalWall w) { // �ӵ��򵽽���ǽ��
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			//this.tc.metalWall.remove(w); //�ӵ����ܴ�Խ����ǽ
			return true;
		}
		return false;
	}

	public boolean hitHome() { // ���ӵ��򵽼�ʱ
		if (this.live && this.getRect().intersects(tc.home.getRect())) {
			this.live = false;
			this.tc.home.setLive(false); // ���ҽ���һǹʱ������
			return true;
		}
		return false;
	}

}
