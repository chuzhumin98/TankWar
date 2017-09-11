import java.awt.*;
import java.awt.event.*;
import java.util.*;

import net.sf.json.JSONArray;

public class Tank {
	public static  int speedX = 6, speedY =6; // 静态全局变量速度---------可以作为扩张来设置级别，速度快的话比较难
	public static int count = 0;
	public static final int width = 35, length = 35; // 坦克的全局大小，具有不可改变性
	Direction direction = Direction.STOP; // 初始化状态为静止
	Direction Kdirection = Direction.U; // 初始化方向为向上
	TankClient tc;
	int score; //所获分数
	boolean ishigher; //是否为高级坦克
	int predtime = 0; //之前的时间
	
	private boolean good;
	int x, y;
	int oldX, oldY;
	private boolean live = true; // 初始化为活着
	private int life = 200; // 初始生命值

	private static Random r = new Random();
	private int step = r.nextInt(10)+5 ; // 产生一个随机数,随机模拟坦克的移动路径

	private boolean bL = false, bU = false, bR = false, bD = false;
	
	void init(int x, int y) {
		this.score = 0;
		live = true;
		this.life = 200;
		this.x = x;
		this.y = y;
		this.Kdirection = Direction.U;
		this.direction = Direction.STOP;
	}

	private static Toolkit tk = Toolkit.getDefaultToolkit();// 控制面板
	private static Image[] tankImags = null; // 存储全局静态
	static {
		tankImags = new Image[] {
				tk.getImage(BombTank.class.getResource("Images/tankD.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankU.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankL.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankR.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankhD.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankhU.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankhL.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankhR.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankmD.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankmU.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankmL.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankmR.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankyD.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankyU.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankyL.gif")),
				tk.getImage(BombTank.class.getResource("Images/TankyR.gif")),
				};

	}

	public Tank(int x, int y, boolean good) {// Tank的构造函数1
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
		this.score = 0;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {// Tank的构造函数2
		this(x, y, good);
		this.direction = dir;
		this.score = 0;
		this.tc = tc;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc, boolean ishigher) {// Tank的构造函数2
		this(x, y, good);
		this.direction = dir;
		this.score = 0;
		this.tc = tc;
		this.ishigher = ishigher;
	}
	
	void setdraws(JSONArray aa1, boolean isone) {
		this.x = aa1.getInt(1);
		this.y = aa1.getInt(2);
		switch(aa1.getInt(0)) {
		case 0:
			this.Kdirection = Direction.D;
			break;
		case 1:
			this.Kdirection = Direction.U;
			break;
		case 2:
			this.Kdirection = Direction.L;
			break;
		case 3:
			this.Kdirection = Direction.R;
			break;
		}
		if (aa1.size() >= 4) {
			if (aa1.getInt(3) == 8) {
				this.ishigher = true;
			} else if (aa1.getInt(3) == 6){
				this.ishigher = false;
			} else {
				this.life = aa1.getInt(3);
			}
		}
		if (aa1.size() >= 5) {
			this.score = aa1.getInt(4);
		}
		if (aa1.size() >= 6) {
			if (aa1.getInt(5) != predtime && ((isone && tc.player == 1) || (!isone && tc.player == 2))) { //找到配对的玩家
				this.predtime = aa1.getInt(5);
				new Thread(new bgMusic2V2()).start(); //发弹时调出枪响音效
			}
		}
	}
	public void drawforus(Graphics g, boolean isone) { //是自己坦克时player时
		if (this.life == 0) {
			return;
		}
		if (isone)
			new DrawBloodbBar().draw(g); // 创造一个血包
		else {
			new DrawBloodbBar().draw2(g);
		}

		switch (Kdirection) {
		//根据方向选择坦克的图片
		case D:
			if (isone) {	
				g.drawImage(tankImags[8], x, y, null); //1号玩家蓝色，2号玩家黄色
			} else {
				g.drawImage(tankImags[12], x, y, null);
			}
			break;

		case U:
			if (isone) {	
				g.drawImage(tankImags[9], x, y, null); //1号玩家蓝色，2号玩家黄色
			} else {
				g.drawImage(tankImags[13], x, y, null);
			}
			break;
		case L:
			if (isone) {	
				g.drawImage(tankImags[10], x, y, null); //1号玩家蓝色，2号玩家黄色
			} else {
				g.drawImage(tankImags[14], x, y, null);
			}
			break;

		case R:
			if (isone) {	
				g.drawImage(tankImags[11], x, y, null); //1号玩家蓝色，2号玩家黄色
			} else {
				g.drawImage(tankImags[15], x, y, null);
			}
			break;

		}

	}
	public void draw(Graphics g) {

		switch (Kdirection) {
							//根据方向选择坦克的图片
		case D:
			if (this.ishigher && !this.good) {
				g.drawImage(tankImags[4], x, y, null);
			}else {
				g.drawImage(tankImags[0], x, y, null);
			}	
			break;

		case U:
			if (this.ishigher && !this.good) {
				g.drawImage(tankImags[5], x, y, null);
			}else {
				g.drawImage(tankImags[1], x, y, null);
			}	
			break;
		case L:
			if (this.ishigher && !this.good) {
				g.drawImage(tankImags[6], x, y, null);
			}else {
				g.drawImage(tankImags[2], x, y, null);
			}	
			break;

		case R:
			if (this.ishigher && !this.good) {
				g.drawImage(tankImags[7], x, y, null);
			}else {
				g.drawImage(tankImags[3], x, y, null);
			}	
			break;

		}
	}

	void move() {

		this.oldX = x;
		this.oldY = y;

		switch (direction) {  //选择移动方向
		case L:
			x -= speedX;
			break;
		case U:
			y -= speedY;
			break;
		case R:
			x += speedX;
			break;
		case D:
			y += speedY;
			break;
		case STOP:
			break;
		}

		if (this.direction != Direction.STOP) {
			this.Kdirection = this.direction;
		}

		if (x < 0)
			x = 0;
		if (y < 40)      //防止走出规定区域
			y = 40;
		if (x + Tank.width > TankClient.Fram_width)  //超过区域则恢复到边界
			x = TankClient.Fram_width - Tank.width;
		if (y + Tank.length > TankClient.Fram_length)
			y = TankClient.Fram_length - Tank.length;

		if (!good) {
			Direction[] directons = Direction.values();
			if (step == 0) {                  
				step = r.nextInt(12) + 3;  //产生随机路径
				int rn = r.nextInt(directons.length);
				direction = directons[rn];      //产生随机方向
			}
			step--;

			if (r.nextInt(40) > 38)//产生随机数，控制敌人开火
				this.fire();
		}
	}

	private void changToOldDir() {  
		x = oldX;
		y = oldY;
	}

	public void keyPressed(KeyEvent e) {  //接受键盘事件
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT: //监听向右键
			bR = true;
			break;
			
		case KeyEvent.VK_LEFT://监听向左键
			bL = true;
			break;
		
		case KeyEvent.VK_UP:  //监听向上键
			bU = true;
			break;
		
		case KeyEvent.VK_DOWN://监听向下键
			bD = true;
			break;
		}
		decideDirection();//调用函数确定移动方向
	}
	
	public int keyPressedforclient(KeyEvent e) {  //接受键盘事件
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_D: //监听向右键
			return 111;
			
		case KeyEvent.VK_A://监听向左键
			return 112;
		
		case KeyEvent.VK_W:  //监听向上键
			return 113;
		
		case KeyEvent.VK_S://监听向下键
			return 114;
		}
		return 115;
	}
	
	public void keyPressed2(KeyEvent e) {  //接受键盘事件
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_D: //监听向右键
			bR = true;
			break;
			
		case KeyEvent.VK_A://监听向左键
			bL = true;
			break;
		
		case KeyEvent.VK_W:  //监听向上键
			bU = true;
			break;
		
		case KeyEvent.VK_S://监听向下键
			bD = true;
			break;
		}
		decideDirection();//调用函数确定移动方向
	}
	
	public int keyPressed2forclient(KeyEvent e) {  //接受键盘事件
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT: //监听向右键
			return 121;

		case KeyEvent.VK_LEFT://监听向左键
			return 122;
		
		case KeyEvent.VK_UP:  //监听向上键
			return 123;
		
		case KeyEvent.VK_DOWN://监听向下键
			return 124;
		}
		return 125;
	}

	void decideDirection() {
		if (!bL && !bU && bR && !bD)  //向右移动
			direction = Direction.R;

		else if (bL && !bU && !bR && !bD)   //向左移
			direction = Direction.L;

		else if (!bL && bU && !bR && !bD)  //向上移动
			direction = Direction.U;

		else if (!bL && !bU && !bR && bD) //向下移动
			direction = Direction.D;

		else if (!bL && !bU && !bR && !bD)
			direction = Direction.STOP;  //没有按键，就保持不动
	}

	public void keyReleased(KeyEvent e) {  //键盘释放监听
		int key = e.getKeyCode();
		switch (key) {
		
		case KeyEvent.VK_F:
			fire();
			break;
			
		case KeyEvent.VK_D:
			bR = false;
			break;
		
		case KeyEvent.VK_A:
			bL = false;
			break;
		
		case KeyEvent.VK_W:
			bU = false;
			break;
		
		case KeyEvent.VK_S:
			bD = false;
			break;
			

		}
		decideDirection();  //释放键盘后确定移动方向
	}
	public int keyReleasedforclient(KeyEvent e) {  //键盘释放监听
		int key = e.getKeyCode();
		switch (key) {
		
		case KeyEvent.VK_F:
			return 210;
			
		case KeyEvent.VK_D:
			return 211;
		
		case KeyEvent.VK_A:
			return 212;
		
		case KeyEvent.VK_W:
			return 213;
		
		case KeyEvent.VK_S:
			return 214;
			
		}
		return 215;
	}
	
	public int keyReleased2forclient(KeyEvent e) {  //键盘释放监听
		int key = e.getKeyCode();
		switch (key) {
		
		case KeyEvent.VK_SPACE:
			return 220;
			
		case KeyEvent.VK_RIGHT:
			return 221;
		
		case KeyEvent.VK_LEFT:
			return 222;
		
		case KeyEvent.VK_UP:
			return 223;
		
		case KeyEvent.VK_DOWN:
			return 224;
			

		}
		return 225;
	}

	public void keyReleased2(KeyEvent e) {  //键盘释放监听
		int key = e.getKeyCode();
		switch (key) {
		
		case KeyEvent.VK_SPACE:
			fire();
			break;
			
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		
		case KeyEvent.VK_UP:
			bU = false;
			break;
		
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
			

		}
		decideDirection();  //释放键盘后确定移动方向
	}
	public Bullets fire() {  //开火方法
		if (!live)
			return null;
		int x = this.x + Tank.width / 2 - Bullets.width / 2;  //开火位置
		int y = this.y + Tank.length / 2 - Bullets.length / 2;
		Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc);  //没有给定方向时，向原来的方向发火
		tc.bullets.add(m);                                                
		return m;
	}


	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public boolean collideWithWall(CommonWall w) {  //碰撞到普通墙时
		if (this.live && this.getRect().intersects(w.getRect())) {
			 this.changToOldDir();    //转换到原来的方向上去
			return true;
		}
		return false;
	}

	public boolean collideWithWall(MetalWall w) {  //撞到金属墙
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();     
			return true;
		}
		return false;
	}

	public boolean collideRiver(River r) {    //撞到河流的时候
		if (this.live && this.getRect().intersects(r.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideHome(Home h) {   //撞到家的时候
		if (this.live && this.getRect().intersects(h.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideWithTanks(java.util.List<Tank> tanks) {//撞到坦克时
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive()
						&& this.getRect().intersects(t.getRect())) {
					this.changToOldDir();
					t.changToOldDir();
					return true;
				}
			}
		}
		return false;
	}


	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private class DrawBloodbBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(375, 574, width, 10);
			int w = width * life / 200;
			g.fillRect(375, 574, w, 10);
			g.setColor(c);
		}
		public void draw2(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(375, 586, width, 10);
			int w = width * life / 200;
			g.fillRect(375, 586, w, 10);
			g.setColor(c);
		}
	}

	public boolean eat(GetBlood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			if(this.life<=100)
			this.life = this.life+100;      //每吃一个，增加100生命点
			else
				this.life = 200;
			b.setLive(false);
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}