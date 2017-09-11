import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class Bullets {
	public static  int speedX = 10;
	public static  int speedY = 10; // 子弹的全局静态速度

	public static final int width = 10;
	public static final int length = 10;

	int x, y;
	Direction diretion;

	private boolean good;
	private boolean live = true;

	private TankClient tc;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] bulletImages = null;
	private static Map<Integer, Image> imgs = new HashMap<Integer, Image>(); // 定义Map键值对，是不同方向对应不同的弹头

	static {
		bulletImages = new Image[] { // 不同方向的子弹
				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletL.gif")),

				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletU.gif")),

				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletR.gif")),

				tk.getImage(Bullets.class.getClassLoader().getResource(
						"Images/bulletD.gif")),

		};

		imgs.put(0, bulletImages[0]); // 加入Map容器

		imgs.put(1, bulletImages[1]);

		imgs.put(2, bulletImages[2]);

		imgs.put(3, bulletImages[3]);

	}

	public Bullets(int x, int y, Direction dir) { // 构造函数1，传递位置和方向
		this.x = x;
		this.y = y;
		this.diretion = dir;
	}

	// 构造函数2，接受另外两个参数
	public Bullets(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}

	private void move() {

		switch (diretion) {
		case L:
			x -= speedX; // 子弹不断向左进攻
			break;

		case U:
			y -= speedY;
			break;

		case R:
			x += speedX; // 字段不断向右
			break;

		case D:
			y += speedY;
			break;

		case STOP:
			break;
		}

		if (x < 0 || y < 0 || x > TankClient.Fram_width
				|| y > TankClient.Fram_length) {
			live = false;
		}
	}
	void setdraw(JSONArray aa1) {
		this.x = aa1.getInt(1);
		this.y = aa1.getInt(2);
		switch(aa1.getInt(0)) {
		case 0:
			this.diretion = Direction.D;
			break;
		case 1:
			this.diretion = Direction.U;
			break;
		case 2:
			this.diretion = Direction.L;
			break;
		case 3:
			this.diretion = Direction.R;
			break;
		}
	}
	public void draw(Graphics g) {
		switch (diretion) { // 选择不同方向的子弹
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
	}

	public boolean isLive() { // 判读是否还活着
		return live;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean hitTanks(List<Tank> tanks) {// 当子弹打到坦克时
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) { // 对每一个坦克，调用hitTank
				return true;
			}
		}
		return false;
	}

	public boolean hitTank(Tank t) { // 当子弹打到坦克上

		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive()
				&& this.good != t.isGood()) {

			BombTank e = new BombTank(t.getX(), t.getY(), tc);
			tc.bombTanks.add(e);
			if (t.isGood()) {
				t.setLife(t.getLife() - 50); // 受一粒子弹寿命减少50，接受4枪就死,总生命值200
				if (t.getLife() <= 0)
					t.setLive(false); // 当寿命为0时，设置寿命为死亡状态
			} else {
				t.setLive(false); 
			}

			this.live = false;

			return true; // 射击成功，返回true
		}
		return false; // 否则返回false
	}

	public boolean hitWall(CommonWall w) { // 子弹打到CommonWall上
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			this.tc.otherWall.remove(w); // 子弹打到CommonWall墙上时则移除此击中墙
			this.tc.homeWall.remove(w);
			return true;
		}
		return false;
	}

	public boolean hitWall(MetalWall w) { // 子弹打到金属墙上
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			//this.tc.metalWall.remove(w); //子弹不能穿越金属墙
			return true;
		}
		return false;
	}

	public boolean hitHome() { // 当子弹打到家时
		if (this.live && this.getRect().intersects(tc.home.getRect())) {
			this.live = false;
			this.tc.home.setLive(false); // 当家接受一枪时就死亡
			return true;
		}
		return false;
	}

}
