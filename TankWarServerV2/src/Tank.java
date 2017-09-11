import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank {
	public static  int speedX = 6, speedY =6; // ��̬ȫ�ֱ����ٶ�---------������Ϊ���������ü����ٶȿ�Ļ��Ƚ���
	public static int count = 0;
	public static final int width = 35, length = 35; // ̹�˵�ȫ�ִ�С�����в��ɸı���
	Direction direction = Direction.STOP; // ��ʼ��״̬Ϊ��ֹ
	Direction Kdirection = Direction.U; // ��ʼ������Ϊ����
	TankServer tc;
	int score; //��÷���
	private boolean good;
	int x, y;
	int oldX, oldY;
	boolean live = true; // ��ʼ��Ϊ����
	int life = 200; // ��ʼ����ֵ
	boolean ishigher; //�Ƿ��Ǹ߼�̹��
	long predtime; //�ϴ������ʱ��
	long succtime; //��������ʱ��
	int predfiring; //֮ǰ��ǹ����
	int succfiring; //���ڿ�ǹ����

	void init(int x, int y) {
		this.score = 0;
		live = true;
		this.life = 200;
		this.x = x;
		this.y = y;
		this.Kdirection = Direction.U;
		this.direction = Direction.STOP;
		this.predtime = System.nanoTime();
		this.predfiring = 0;
		this.succfiring = 0;
	}
	
	private static Random r = new Random();
	private int step = r.nextInt(10)+5 ; // ����һ�������,���ģ��̹�˵��ƶ�·��

	private boolean bL = false, bU = false, bR = false, bD = false;
	

	private static Toolkit tk = Toolkit.getDefaultToolkit();// �������
	private static Image[] tankImags = null; // �洢ȫ�־�̬
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
				tk.getImage(BombTank.class.getResource("Images/TankyR.gif")),};

	}

	public Tank(int x, int y, boolean good) {// Tank�Ĺ��캯��1
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
		this.score = 0;
		this.predtime = System.nanoTime();
		this.predfiring = 0;
		this.succfiring = 0;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankServer tc) {// Tank�Ĺ��캯��2
		this(x, y, good);
		this.direction = dir;
		this.tc = tc;
		this.score = 0;
		if (good) {
			this.life = 200;
		} else {
			this.life = 50; //Ĭ��Ϊ��̹ͨ��
		}
	}
	public Tank(int x, int y, boolean good, Direction dir, TankServer tc, boolean ishigher) {// Tank�Ĺ��캯��2
		this(x, y, good);
		this.direction = dir;
		this.tc = tc;
		this.score = 0;
		this.ishigher = ishigher;
		if (ishigher) {
			this.life = 100; //�߼�̹��Ѫ��100
		} else {
			this.life = 50;
		}
	}

	public void drawforus(Graphics g, boolean isone) { //���Լ�̹��ʱplayerʱ
		if (!live) {
			if (!good) {
				tc.tanks.remove(this); // ɾ����Ч��
			}
			return;
		}

		if (isone)
			new DrawBloodbBar().draw(g); // ����һ��Ѫ��
		else {
			new DrawBloodbBar().draw2(g);
		}

		switch (Kdirection) {
							//���ݷ���ѡ��̹�˵�ͼƬ
		case D:
			if (isone) {	
				g.drawImage(tankImags[8], x, y, null); //1�������ɫ��2����һ�ɫ
			} else {
				g.drawImage(tankImags[12], x, y, null);
			}
			break;

		case U:
			if (isone) {	
				g.drawImage(tankImags[9], x, y, null); //1�������ɫ��2����һ�ɫ
			} else {
				g.drawImage(tankImags[13], x, y, null);
			}
			break;
		case L:
			if (isone) {	
				g.drawImage(tankImags[10], x, y, null); //1�������ɫ��2����һ�ɫ
			} else {
				g.drawImage(tankImags[14], x, y, null);
			}
			break;

		case R:
			if (isone) {	
				g.drawImage(tankImags[11], x, y, null); //1�������ɫ��2����һ�ɫ
			} else {
				g.drawImage(tankImags[15], x, y, null);
			}
			break;

		}

		move();   //����move����
	}
	
	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this); // ɾ����Ч��
			}
			return;
		}

		if (good)
			new DrawBloodbBar().draw(g); // ����һ��Ѫ��

		switch (Kdirection) {
							//���ݷ���ѡ��̹�˵�ͼƬ
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

		move();   //����move����
	}

	void move() {

		this.oldX = x;
		this.oldY = y;

		switch (direction) {  //ѡ���ƶ�����
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
		if (y < 40)      //��ֹ�߳��涨����
			y = 40;
		if (x + Tank.width > TankServer.Fram_width)  //����������ָ����߽�
			x = TankServer.Fram_width - Tank.width;
		if (y + Tank.length > TankServer.Fram_length)
			y = TankServer.Fram_length - Tank.length;

		if (!good) {
			Direction[] directons = Direction.values();
			if (step == 0) {                  
				step = r.nextInt(12) + 3;  //�������·��
				int rn = r.nextInt(directons.length);
				direction = directons[rn];      //�����������
			}
			step--;

			if (r.nextInt(40) > 38)//��������������Ƶ��˿���
				this.fire();
		}
	}

	private void changToOldDir() {  
		x = oldX;
		y = oldY;
	}

	public void keyPressed(KeyEvent e) {  //���ܼ����¼�
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT: //�������Ҽ�
			bR = true;
			break;
			
		case KeyEvent.VK_LEFT://���������
			bL = true;
			break;
		
		case KeyEvent.VK_UP:  //�������ϼ�
			bU = true;
			break;
		
		case KeyEvent.VK_DOWN://�������¼�
			bD = true;
			break;
		}
		decideDirection();//���ú���ȷ���ƶ�����
	}
	
	public void keyPressedforserver(int code) {  //���ܼ����¼�
		switch (code) {
		case 111: //�������Ҽ�
			bR = true;
			break;
			
		case 112://���������
			bL = true;
			break;
		
		case 113:  //�������ϼ�
			bU = true;
			break;
		
		case 114://�������¼�
			bD = true;
			break;
		}
		decideDirection();//���ú���ȷ���ƶ�����
	}
	
	public void keyPressed2forserver(int code) {  //���ܼ����¼�
		switch (code) {
		case 121: //�������Ҽ�
			bR = true;
			break;
			
		case 122://���������
			bL = true;
			break;
		
		case 123:  //�������ϼ�
			bU = true;
			break;
		
		case 124://�������¼�
			bD = true;
			break;
		}
		decideDirection();//���ú���ȷ���ƶ�����
	}
	
	public void keyPressed2(KeyEvent e) {  //���ܼ����¼�
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_D: //�������Ҽ�
			bR = true;
			break;
			
		case KeyEvent.VK_A://���������
			bL = true;
			break;
		
		case KeyEvent.VK_W:  //�������ϼ�
			bU = true;
			break;
		
		case KeyEvent.VK_S://�������¼�
			bD = true;
			break;
		}
		decideDirection();//���ú���ȷ���ƶ�����
	}

	void decideDirection() {
		if (!bL && !bU && bR && !bD)  //�����ƶ�
			direction = Direction.R;

		else if (bL && !bU && !bR && !bD)   //������
			direction = Direction.L;

		else if (!bL && bU && !bR && !bD)  //�����ƶ�
			direction = Direction.U;

		else if (!bL && !bU && !bR && bD) //�����ƶ�
			direction = Direction.D;

		else if (!bL && !bU && !bR && !bD)
			direction = Direction.STOP;  //û�а������ͱ��ֲ���
	}

	public void keyReleased(KeyEvent e) {  //�����ͷż���
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
		decideDirection();  //�ͷż��̺�ȷ���ƶ�����
	}
	
	public void keyReleasedforserver(int code) {  //�����ͷż���
		switch (code) {
		
		case 210:
			fire();
			break;
			
		case 211:
			bR = false;
			break;
		
		case 212:
			bL = false;
			break;
		
		case 213:
			bU = false;
			break;
		
		case 214:
			bD = false;
			break;
			

		}
		decideDirection();  //�ͷż��̺�ȷ���ƶ�����
	}

	public void keyReleased2forserver(int code) {  //�����ͷż���
		switch (code) {
		
		case 220:
			fire();
			break;
			
		case 221:
			bR = false;
			break;
		
		case 222:
			bL = false;
			break;
		
		case 223:
			bU = false;
			break;
		
		case 224:
			bD = false;
			break;
			

		}
		decideDirection();  //�ͷż��̺�ȷ���ƶ�����
	}
	
	public void keyReleased2(KeyEvent e) {  //�����ͷż���
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
		decideDirection();  //�ͷż��̺�ȷ���ƶ�����
	}
	public Bullets fire() {  //���𷽷�
		if (!live)
			return null;
		if (this.good) { //���Լ���̹���������������ʱ������Ϊ1s���Ե���û������
			this.succtime = System.nanoTime();
			if (this.succtime - this.predtime >= 1000000000) { //���1s�����
				this.predtime = this.succtime;
			} else {
				if (this.succtime < this.predtime) {
					this.predtime = this.succtime; //ʱ����������޸�
					return null;
				}
				return null;
			}
		}
		if (this.good) {
			this.succfiring++; //��Ҫ����һ�η�������
		}
		int x = this.x + Tank.width / 2 - Bullets.width / 2;  //����λ��
		int y = this.y + Tank.length / 2 - Bullets.length / 2;
		Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc, this);  //û�и�������ʱ����ԭ���ķ��򷢻�
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

	public boolean collideWithWall(CommonWall w) {  //��ײ����ͨǽʱ
		if (this.live && this.getRect().intersects(w.getRect())) {
			 this.changToOldDir();    //ת����ԭ���ķ�����ȥ
			return true;
		}
		return false;
	}

	public boolean collideWithWall(MetalWall w) {  //ײ������ǽ
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();     
			return true;
		}
		return false;
	}

	public boolean collideRiver(River r) {    //ײ��������ʱ��
		if (this.live && this.getRect().intersects(r.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideHome(Home h) {   //ײ���ҵ�ʱ��
		if (this.live && this.getRect().intersects(h.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideWithTank(Tank t) {//ײ��̹��ʱ
		if (this != t) {
			if (this.live && t.isLive()
					&& this.getRect().intersects(t.getRect())) {
				this.changToOldDir();
				t.changToOldDir();
				return true;
			}
		}
		return false;
	}
	
	public boolean collideWithTanks(java.util.List<Tank> tanks) {//ײ��̹��ʱ
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
			this.life = this.life+100;      //ÿ��һ��������100������
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