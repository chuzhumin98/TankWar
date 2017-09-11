import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;

import net.sf.json.JSONObject;
/*
river:x y
hometank,tanks: Kdirection(0~3) x y
Bullets: diretion x y
homeWall,otherWall: x y
metalWall: x y
trees: x y
blood: x y
BombTank: step x y
*/
public class TankServer extends Frame implements ActionListener {
	
	
	/**
	 * 
	 */
	int count = 0;
	boolean iswrittern = false;
	private static final long serialVersionUID = 1L;
	String player1nickname; //���1���ǳ�
	String player2nickname;
	public static final int Fram_width = 800; // ��̬ȫ�ִ��ڴ�С
	public static final int Fram_length = 600;
	public static boolean printable = true;
	ArrayList<String> top5s = new ArrayList<String>(); //����top5���ǳ�
	ArrayList<String> top5i = new ArrayList<String>(); //����top5�ĵ÷�
 	MenuBar jmb = null;
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null;
	Image screenImage = null;

	Tank homeTank,homeTank2;
	GetBlood blood;
	Home home;

	List<River> theRiver = new ArrayList<River>();
	List<Tank> tanks = new ArrayList<Tank>();
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	List<Bullets> bullets = new ArrayList<Bullets>();
	List<Tree> trees = new ArrayList<Tree>();
	List<CommonWall> homeWall = new ArrayList<CommonWall>(); // ʵ������������
	List<CommonWall> otherWall = new ArrayList<CommonWall>();
	List<MetalWall> metalWall = new ArrayList<MetalWall>();
	List<BombTank> bombfortransi = new ArrayList<BombTank>();

	
	String TransiJSON() {
		try { 
			JSONObject jso1 = new JSONObject();
			ArrayList<ArrayList> a1;
			
			ArrayList<Integer> aa1 = new ArrayList<Integer>();
			switch (homeTank.Kdirection) {
			case D:
				aa1.add(0);
				break;

			case U:
				aa1.add(1);
				break;
			case L:
				aa1.add(2);
				break;

			case R:
				aa1.add(3);
				break;
			}
			aa1.add(homeTank.x);
			aa1.add(homeTank.y);
			aa1.add(homeTank.life);
			aa1.add(homeTank.score);
			aa1.add(homeTank.succfiring);
			jso1.put("homeTank", aa1);
			
			aa1 = new ArrayList<Integer>();
			switch (homeTank2.Kdirection) {
			case D:
				aa1.add(0);
				break;

			case U:
				aa1.add(1);
				break;
			case L:
				aa1.add(2);
				break;

			case R:
				aa1.add(3);
				break;
			}
			aa1.add(homeTank2.x);
			aa1.add(homeTank2.y);
			aa1.add(homeTank2.life);
			aa1.add(homeTank2.score);
			aa1.add(homeTank2.succfiring);
			jso1.put("homeTank2", aa1);
					
			a1 = new ArrayList<ArrayList>();
			for (int i = 0; i < bullets.size(); i++) {
				Bullets m = bullets.get(i);
				aa1 = new ArrayList<Integer>();
				switch (m.diretion) {
				case D:
					aa1.add(0);
					break;

				case U:
					aa1.add(1);
					break;
				case L:
					aa1.add(2);
					break;
				case R:
					aa1.add(3);
					break;
				}
				aa1.add(m.x);
				aa1.add(m.y);
				a1.add(aa1);
			}
			jso1.put("Bullets", a1); 
			
			a1 = new ArrayList<ArrayList>();
			for (int j = 0; j < otherWall.size(); j++) {// ÿһ���ӵ�������ǽ��
				CommonWall w = otherWall.get(j);
				aa1 = new ArrayList<Integer>();
				aa1.add(w.x);
				aa1.add(w.y);
				a1.add(aa1);
			}
			jso1.put("otherWall", a1); 
			
			a1 = new ArrayList<ArrayList>();
			for (int j = 0; j < metalWall.size(); j++) {// ÿһ���ӵ�������ǽ��
				MetalWall w = metalWall.get(j);
				aa1 = new ArrayList<Integer>();
				aa1.add(w.x);
				aa1.add(w.y);
				a1.add(aa1);
			}
			jso1.put("metalWall", a1); 
			
			a1 = new ArrayList<ArrayList>();
			for (int j = 0; j < trees.size(); j++) {// ÿһ���ӵ�������ǽ��
				Tree w = trees.get(j);
				aa1 = new ArrayList<Integer>();
				aa1.add(w.x);
				aa1.add(w.y);
				a1.add(aa1);
			}
			jso1.put("trees", a1); 
			
			a1 = new ArrayList<ArrayList>();
			for (int j = 0; j < this.theRiver.size(); j++) {// ÿһ���ӵ�������ǽ��
				River w = theRiver.get(j);
				aa1 = new ArrayList<Integer>();
				aa1.add(w.x);
				aa1.add(w.y);
				a1.add(aa1);
			}
			jso1.put("river", a1); 
			
			a1 = new ArrayList<ArrayList>();
			for (int j = 0; j < homeWall.size(); j++) {// ÿһ���ӵ��򵽼ҵ�ǽ��
				CommonWall cw = homeWall.get(j);
				aa1 = new ArrayList<Integer>();
				aa1.add(cw.x);
				aa1.add(cw.y);
				a1.add(aa1);
			}
			jso1.put("homeWall", a1); 
			
			a1 = new ArrayList<ArrayList>();
			for (int i = 0; i < tanks.size(); i++) {
				Tank t = tanks.get(i); // ��ü�ֵ�Եļ�
				aa1 = new ArrayList<Integer>();
				switch (t.Kdirection) {
				case D:
					aa1.add(0);
					break;

				case U:
					aa1.add(1);
					break;
				case L:
					aa1.add(2);
					break;

				case R:
					aa1.add(3);
					break;
				}
				aa1.add(t.x);
				aa1.add(t.y);
				if (t.ishigher) {
					aa1.add(8); //8�����߼�̹��
				} else {
					aa1.add(6);
				}
				a1.add(aa1);
				
			}
			jso1.put("tanks", a1);
			
			aa1 = new ArrayList<Integer>();
			if (blood.live) {
				aa1.add(1);
			} else {
				aa1.add(0);
			}
			aa1.add(blood.x);
			aa1.add(blood.y);
			jso1.put("blood", aa1);
			
			a1 = new ArrayList<ArrayList>();
			for (int i = 0; i < this.bombfortransi.size(); i++) { // ������ըЧ��
				BombTank bt = this.bombfortransi.get(i);
				aa1 = new ArrayList<Integer>();
				aa1.add(bt.x);
				aa1.add(bt.y);
				a1.add(aa1);
			}
			count = bombfortransi.size();
			System.out.println("count:"+count);
			//this.bombfortransi.clear();
			jso1.put("bombTanks", a1);
			
			if (tanks.size() == 0 && ((homeTank.isLive() || homeTank2.isLive()) && home.live)) {
				jso1.put("isover", 1); //1�����Ѿ�Ӯ��
			} else if (homeTank.isLive() == false && homeTank2.isLive() == false) {
				jso1.put("isover", -1); //-1�����Ѿ�����
			} else {
				jso1.put("isover", 0); //0���������û�н���
			}
			
			if (home.live) {
				jso1.put("Home", 1); //�һ�������Ϊ1
			} else {
				jso1.put("Home", 0); //������Ϊ0
			}
			
			jso1.put("top5s", this.top5s);
			jso1.put("top5i", this.top5i);
			
			
			
			//System.out.println("JSON"+jso1.toString());
			return jso1.toString();
		} catch(Exception e) {
			System.out.println("Fail to create JSON");
			return null;
		}
		
		
	}
	public void update(Graphics g) {
		
		screenImage = this.createImage(Fram_width, Fram_length);

		Graphics gps = screenImage.getGraphics();
		Color c = gps.getColor();
		gps.setColor(Color.GRAY);
		gps.fillRect(0, 0, Fram_width, Fram_length);
		gps.setColor(c);
		framPaint(gps);		
		g.drawImage(screenImage, 0, 0, null); 
		TransiJSON();
	}
    
	public void framPaint(Graphics g) {

		Color c = g.getColor();
		g.setColor(Color.green); // ����������ʾ����

		Font f1 = g.getFont();
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("�����ڻ��ез�̹��: ", 100, 70);
		g.setFont(new Font("TimesRoman", Font.ITALIC, 20));
		g.drawString("" + tanks.size(), 300, 70);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("player 1ʣ������ֵ: ", 350, 70);
		g.drawString("player 2ʣ������ֵ: ", 350, 90);
		g.drawString("����: ", 600, 70);
		g.drawString("����: ", 600, 90);
		g.setFont(new Font("TimesRoman", Font.ITALIC, 20));
		g.drawString("" + homeTank.getLife(), 550, 70);
		g.drawString("" + homeTank2.getLife(), 550, 90);
		g.drawString("" + homeTank.score, 660, 70);
		g.drawString("" + homeTank2.score, 660, 90);
		g.setFont(f1);

		if (tanks.size() == 0 && ((homeTank.isLive() || homeTank2.isLive()) && home.isLive())) {
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 40)); // �ж��Ƿ�Ӯ�ñ���
			this.otherWall.clear();
			this.trees.clear();
			this.tanks.clear();
			this.theRiver.clear();
			this.metalWall.clear();
			g.drawString("��Ӯ�ˣ� ", 100, 300);
			g.setFont(f);
			if (!this.iswrittern) {
				String[] stmp = {this.player1nickname,this.player2nickname};
				int[] itmp = {homeTank.score, homeTank2.score};
				aboutFile af1 = new aboutFile();
				af1.run(g, stmp, itmp);
				this.top5s = af1.top5s;
				this.top5i = af1.top5i;
				this.iswrittern = true;
			} else {
				new aboutFile().showprint(g);
			}
		}

		if (homeTank.isLive() == false && homeTank2.isLive() == false) {
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 40));
			tanks.clear();
			bullets.clear();
			this.otherWall.clear();
			this.trees.clear();
			this.theRiver.clear();
			this.metalWall.clear();
			g.drawString("�����ˣ�", 100, 250);
			g.drawString("  ��Ϸ������ ", 100, 300);
			g.setFont(f);
			if (!this.iswrittern) {
				String[] stmp = {this.player1nickname,this.player2nickname};
				int[] itmp = {homeTank.score, homeTank2.score};
				aboutFile af1 = new aboutFile();
				af1.run(g, stmp, itmp);
				this.top5s = af1.top5s;
				this.top5i = af1.top5i;
				this.iswrittern = true;
			} else {
				new aboutFile().showprint(g);
			}
			
		}
		g.setColor(c);


		for (int i = 0; i < theRiver.size(); i++) {
			River r = theRiver.get(i);
			homeTank.collideRiver(r);
			homeTank2.collideRiver(r);
			//r.draw(g);
		}

		home.draw(g); // ����home
		homeTank.drawforus(g,true);// �����Լ��ҵ�̹��
		homeTank2.drawforus(g,false);
		homeTank.eat(blood);// ����Ѫ--����ֵ
		homeTank2.eat(blood);// ����Ѫ--����ֵ

		for (int i = 0; i < bullets.size(); i++) { // ��ÿһ���ӵ�
			Bullets m = bullets.get(i);
			m.hitTanks(tanks); // ÿһ���ӵ���̹����
			m.hitTank(homeTank); // ÿһ���ӵ����Լ��ҵ�̹����ʱ
			m.hitTank(homeTank2); // ÿһ���ӵ����Լ��ҵ�̹����ʱ
			m.hitHome(); // ÿһ���ӵ��򵽼�����

			for (int j = 0; j < metalWall.size(); j++) { // ÿһ���ӵ��򵽽���ǽ��
				MetalWall mw = metalWall.get(j);
				m.hitWall(mw);
			}

			for (int j = 0; j < otherWall.size(); j++) {// ÿһ���ӵ�������ǽ��
				CommonWall w = otherWall.get(j);
				m.hitWall(w);
			}

			for (int j = 0; j < homeWall.size(); j++) {// ÿһ���ӵ��򵽼ҵ�ǽ��
				CommonWall cw = homeWall.get(j);
				m.hitWall(cw);
			}
			m.draw(g); // ����Ч��ͼ
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i); // ��ü�ֵ�Եļ�

			for (int j = 0; j < homeWall.size(); j++) {
				CommonWall cw = homeWall.get(j);
				t.collideWithWall(cw); // ÿһ��̹��ײ�������ǽʱ
				//cw.draw(g);
			}
			for (int j = 0; j < otherWall.size(); j++) { // ÿһ��̹��ײ���������ǽ
				CommonWall cw = otherWall.get(j);
				t.collideWithWall(cw);
				//cw.draw(g);
			}
			for (int j = 0; j < metalWall.size(); j++) { // ÿһ��̹��ײ������ǽ
				MetalWall mw = metalWall.get(j);
				t.collideWithWall(mw);
				//mw.draw(g);
			}
			for (int j = 0; j < theRiver.size(); j++) {
				River r = theRiver.get(j); // ÿһ��̹��ײ������ʱ
				t.collideRiver(r);
				r.draw(g);
				// t.draw(g);
			}

			t.collideWithTanks(tanks); // ײ���Լ�����
			t.collideHome(home);

			t.draw(g);
		}

		blood.draw(g);

		for (int i = 0; i < trees.size(); i++) { // ����trees
			Tree tr = trees.get(i);
			tr.draw(g);
		}

		for (int i = 0; i < bombTanks.size(); i++) { // ������ըЧ��
			BombTank bt = bombTanks.get(i);
			bt.draw(g);
		}

		homeTank.collideWithTanks(tanks);
		homeTank2.collideWithTanks(tanks);
		homeTank.collideWithTank(homeTank2);
		homeTank.collideHome(home);
		homeTank2.collideWithTanks(tanks);

		for (int i = 0; i < metalWall.size(); i++) {// ײ������ǽ
			MetalWall w = metalWall.get(i);
			homeTank.collideWithWall(w);
			homeTank2.collideWithWall(w);
			w.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {
			CommonWall cw = otherWall.get(i);
			homeTank.collideWithWall(cw);
			homeTank2.collideWithWall(cw);
			cw.draw(g);
		}

		for (int i = 0; i < homeWall.size(); i++) { // �����̹��ײ���Լ���
			CommonWall w = homeWall.get(i);
			homeTank.collideWithWall(w);
			homeTank2.collideWithWall(w);
			w.draw(g);
		}

	}

	public TankServer() {
		try{
			homeTank = new Tank(300, 560, true, Direction.STOP, this);// ʵ����̹��
			homeTank2 = new Tank(450, 560, true, Direction.STOP, this);// ʵ����̹��
			blood = new GetBlood(); // ʵ��������
			home = new Home(373, 538, this);// ʵ����home
		}catch(Exception e) {
			System.out.println("��ʼ������");
		}
		printable = false;
		// �����˵����˵�ѡ��
		jmb = new MenuBar();
		jm1 = new Menu("��Ϸ");
		jm2 = new Menu("��ͣ/����");
		jm3 = new Menu("����");
		jm4 = new Menu("��Ϸ����");
		jm1.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
		jm2.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
		jm3.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
		jm4.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������

		jmi1 = new MenuItem("��ʼ����Ϸ");
		jmi2 = new MenuItem("�˳�");
		jmi3 = new MenuItem("��ͣ");
		jmi4 = new MenuItem("����");
		jmi5 = new MenuItem("��Ϸ˵��");
		jmi6 = new MenuItem("����1");
		jmi7 = new MenuItem("����2");
		jmi8 = new MenuItem("����3");
		jmi9 = new MenuItem("����4");
		jmi1.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi2.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi3.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi4.setFont(new Font("TimesRoman", Font.BOLD, 15));
		jmi5.setFont(new Font("TimesRoman", Font.BOLD, 15));

		jm1.add(jmi1);
		jm1.add(jmi2);
		jm2.add(jmi3);
		jm2.add(jmi4);
		jm3.add(jmi5);
		jm4.add(jmi6);
		jm4.add(jmi7);
		jm4.add(jmi8);
		jm4.add(jmi9);

		jmb.add(jm1);
		jmb.add(jm2);

		jmb.add(jm4);
		jmb.add(jm3);

		//jmi1.addActionListener(this);
		jmi1.setActionCommand("NewGame");
		//jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		//jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		//jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		//jmi5.addActionListener(this);
		jmi5.setActionCommand("help");
		//jmi6.addActionListener(this);
		jmi6.setActionCommand("level1");
		//jmi7.addActionListener(this);
		jmi7.setActionCommand("level2");
		//jmi8.addActionListener(this);
		jmi8.setActionCommand("level3");
		//jmi9.addActionListener(this);
		jmi9.setActionCommand("level4");

		this.setMenuBar(jmb);// �˵�Bar�ŵ�JFrame��
		this.setVisible(true);

		for (int i = 0; i < 10; i++) { // �ҵĸ��
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(220 + 20 * i, 300, this)); // ��ͨǽ����
				otherWall.add(new CommonWall(500 + 20 * i, 180, this));
				otherWall.add(new CommonWall(200, 400 + 20 * i, this));
				otherWall.add(new CommonWall(500, 400 + 20 * i, this));
			} else if (i < 32) {
				otherWall.add(new CommonWall(220 + 20 * (i - 16), 320, this));
				otherWall.add(new CommonWall(500 + 20 * (i - 16), 220, this));
				otherWall.add(new CommonWall(220, 400 + 20 * (i - 16), this));
				otherWall.add(new CommonWall(520, 400 + 20 * (i - 16), this));
			}
		}

		for (int i = 0; i < 20; i++) { // ����ǽ����
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));
			else
				metalWall.add(new MetalWall(500 + 30 * (i - 10), 160, this));
		}

		for (int i = 0; i < 4; i++) { // ���Ĳ���
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(220 + 30 * i, 360, this));
				trees.add(new Tree(440 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}

		}

		theRiver.add(new River(85, 100, this));

		for (int i = 0; i < 20; i++) { // ��ʼ��20��̹��
			if (i < 9) // ����̹�˳��ֵ�λ��
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this, false));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this, false));
			else
				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this, true)); //��������ߵ�6��̹��Ϊ�߼�̹��
		}

		this.setSize(Fram_width, Fram_length); // ���ý����С
		this.setLocation(280, 50); // ���ý�����ֵ�λ��
		this.setTitle("̹�˴�ս˫�˰�����");

		this.addWindowListener(new WindowAdapter() { // ���ڼ����ر�
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.setVisible(true);

//		this.addKeyListener(new KeyMonitor());// ���̼��� ,�ڷ����Ӧ�ý���
		new Thread(new PaintThread()).start(); // �߳�����
	}
	
	public void init() { //�ָ���ʼ����
		//printable = false;
		this.iswrittern = false;
		homeWall.clear();
		this.bombfortransi.clear();
		for (int i = 0; i < 10; i++) { // �ҵĸ��
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		otherWall.clear();
		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(220 + 20 * i, 300, this)); // ��ͨǽ����
				otherWall.add(new CommonWall(500 + 20 * i, 180, this));
				otherWall.add(new CommonWall(200, 400 + 20 * i, this));
				otherWall.add(new CommonWall(500, 400 + 20 * i, this));
			} else if (i < 32) {
				otherWall.add(new CommonWall(220 + 20 * (i - 16), 320, this));
				otherWall.add(new CommonWall(500 + 20 * (i - 16), 220, this));
				otherWall.add(new CommonWall(220, 400 + 20 * (i - 16), this));
				otherWall.add(new CommonWall(520, 400 + 20 * (i - 16), this));
			}
		}

		metalWall.clear();
		for (int i = 0; i < 20; i++) { // ����ǽ����
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));
			else
				metalWall.add(new MetalWall(500 + 30 * (i - 10), 160, this));
		}

		trees.clear();
		for (int i = 0; i < 4; i++) { // ���Ĳ���
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(220 + 30 * i, 360, this));
				trees.add(new Tree(440 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}

		}

		theRiver.clear();
		theRiver.add(new River(85, 100, this));

		tanks.clear();
		for (int i = 0; i < 20; i++) { // ��ʼ��20��̹��
			if (i < 9) // ����̹�˳��ֵ�λ��
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this, false));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this, false));
			else
				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this, true));
		}
		this.bullets.clear();
		this.home.live = true;
		this.homeTank.init(300, 560);
		this.homeTank2.init(450, 560);
	}

	public static void main(String[] args) {
		new TankServer(); // ʵ����
	}

	private class PaintThread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) { // ���������ͷ�
			homeTank.keyReleased(e);
			homeTank2.keyReleased2(e);
		}

		public void keyPressed(KeyEvent e) { // �������̰���
			homeTank.keyPressed(e);
			homeTank2.keyPressed2(e);
		}

	}

	void started() {
		if (!printable) {
			printable = true;
			new Thread(new PaintThread()).start(); // �߳�����
		}
	}
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ��ʼ����Ϸ��", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {

				printable = true;
				this.dispose();
				new TankServer();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;
			// try {
			// Thread.sleep(10000);
			//
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		} else if (e.getActionCommand().equals("Continue")) {

			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}
			// System.out.println("����");
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ�˳���", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("�˳�");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}

		} else if (e.getActionCommand().equals("help")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "���1ͨ��w,s,a,d����̹�˵����������ƶ���ͨ��f����̹����������2ͨ����,��,��,������̹�˵����������ƶ���ͨ���ո�(space)����̹�����",
					"��ʾ��", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // �߳�����
		} else if (e.getActionCommand().equals("level1")) {
			Tank.count = 12;
			Tank.speedX = 6;
			Tank.speedY = 6;
			Bullets.speedX = 10;
			Bullets.speedY = 10;
			//this.dispose();
			//new TankServer();
		} else if (e.getActionCommand().equals("level2")) {
			Tank.count = 12;
			Tank.speedX = 10;
			Tank.speedY = 10;
			Bullets.speedX = 12;
			Bullets.speedY = 12;
			//this.dispose();
			//new TankServer();

		} else if (e.getActionCommand().equals("level3")) {
			Tank.count = 20;
			Tank.speedX = 14;
			Tank.speedY = 14;
			Bullets.speedX = 16;
			Bullets.speedY = 16;
			//this.dispose();
			//new TankServer();
		} else if (e.getActionCommand().equals("level4")) {
			Tank.count = 20;
			Tank.speedX = 16;
			Tank.speedY = 16;
			Bullets.speedX = 18;
			Bullets.speedY = 18;
			//this.dispose();
			//new TankServer();
		}
	}
}
