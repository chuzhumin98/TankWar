import java.util.ArrayList;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.json.JSONArray;
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
/*
参数传递规则： 
*/
public class TankClient extends Frame implements ActionListener {
	
	
	/**
	 * 
	 */
	int countbomb = 0;
	TCPClientin t1;
	int player; //玩家编号1或2
	boolean isstarted = false;
	String address;
	private static final long serialVersionUID = 1L;
	public static final int Fram_width = 800; // 静态全局窗口大小
	public static final int Fram_length = 600;
	public static boolean printable = true;
	String jsstring; //存储json传输的数据
	JSONArray top5s = new JSONArray(); //排在top5的昵称
	JSONArray top5i = new JSONArray(); //排在top5的得分
	

	
	MenuBar jmb = null;
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null;
	Image screenImage = null;

	Tank homeTank = new Tank(300, 560, true, Direction.STOP, this);// 实例化坦克
	Tank homeTank2 = new Tank(450, 560, true, Direction.STOP, this);// 实例化坦克
	GetBlood blood = new GetBlood(); // 实例化生命
	Home home = new Home(373, 538, this);// 实例化home
	String nickname; //昵称

	List<River> theRiver = new ArrayList<River>();
	List<Tank> tanks = new ArrayList<Tank>();
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	List<Bullets> bullets = new ArrayList<Bullets>();
	List<Tree> trees = new ArrayList<Tree>();
	List<CommonWall> homeWall = new ArrayList<CommonWall>(); // 实例化对象容器
	List<CommonWall> otherWall = new ArrayList<CommonWall>();
	List<MetalWall> metalWall = new ArrayList<MetalWall>();
	int havereadbomb = 0; //记录已经操作过的bombtank的个数



	public void update(Graphics g) {

		screenImage = this.createImage(Fram_width, Fram_length);

		Graphics gps = screenImage.getGraphics();
		Color c = gps.getColor();
		gps.setColor(Color.GRAY);
		gps.fillRect(0, 0, Fram_width, Fram_length);
		gps.setColor(c);
		JSONObject jso1 = JSONObject.fromObject(jsstring);
		framPaint(gps, jso1);
		g.drawImage(screenImage, 0, 0, null); 
		
		//TransiJSON();
	}
    
	public void framPaint(Graphics g, JSONObject jso1) {
		Color c = g.getColor();

		try {
			
		int isover = jso1.getInt("isover");
		if (isover == 1) {
			g.setColor(Color.green); // 设置字体显示属性
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 40)); // 判断是否赢得比赛
			this.otherWall.clear();
			this.trees.clear();
			this.tanks.clear();
			this.theRiver.clear();
			this.metalWall.clear();
			g.drawString("你赢了！ ", 100, 300);
			g.setFont(f);
			
			g.setColor(Color.green); // 设置字体显示属性
			g.setFont(new Font("TimesRoman", Font.BOLD, 35)); 
			g.drawString("Top 5 ", 350, 200);
			g.setFont(new Font("TimesRoman", Font.BOLD, 25)); 
			if (jso1.get("top5s") != null) {
				this.top5s = JSONArray.fromObject(jso1.get("top5s"));
				this.top5i = JSONArray.fromObject(jso1.get("top5i"));
			}
			for (int i = 0; i < top5s.size(); i++) {
				g.drawString(top5s.getString(i), 300, 260+i*30);
				g.drawString(top5i.getString(i), 395, 260+i*30);
			}
		}

		if (isover == -1) {
			g.setColor(Color.green); // 设置字体显示属性
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 40));
			this.otherWall.clear();
			this.trees.clear();
			this.tanks.clear();
			this.theRiver.clear();
			this.metalWall.clear();
			g.drawString("你输了！", 100, 250);
			g.drawString("  游戏结束！ ", 100, 300);
			tanks.clear();
			bullets.clear();
			g.setFont(f);
			
			g.setColor(Color.green); // 设置字体显示属性
			g.setFont(new Font("TimesRoman", Font.BOLD, 35)); 
			g.drawString("Top 5 ", 350, 200);
			g.setFont(new Font("TimesRoman", Font.BOLD, 25)); 
			if (jso1.get("top5s") != null) {
				this.top5s = JSONArray.fromObject(jso1.get("top5s"));
				this.top5i = JSONArray.fromObject(jso1.get("top5i"));
			}
			for (int i = 0; i < top5s.size(); i++) {
				g.drawString(top5s.getString(i), 300, 260+i*30);
				g.drawString(top5i.getString(i), 395, 260+i*30);
			}
			System.out.println("top5:"+top5s.size());
			
		}
		g.setColor(c);

		JSONArray aa1 = JSONArray.fromObject(jso1.get("homeTank"));
		//System.out.println(aa1.get(0));
		if (jso1.getInt("Home") == 0) {
			home.live = false;
		} else {
			home.live = true;
		}
		home.draw(g, jso1); // 画出home
		homeTank.setdraws(aa1, true);

		homeTank.drawforus(g,true);// 画出自己家的坦克

		aa1 = JSONArray.fromObject(jso1.get("homeTank2"));
		homeTank2.setdraws(aa1, false);
		homeTank2.drawforus(g,false);
		JSONArray a1;
		a1 = JSONArray.fromObject(jso1.get("Bullets"));
		for (int i = 0; i < a1.size(); i++) { // 对每一个子弹
			if (bullets.size() < i+1) {
				Bullets tmp = new Bullets(0, 0, Direction.D);
				bullets.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			Bullets m = bullets.get(i);
			m.setdraw(aa1);
			m.draw(g); // 画出效果图
		}

		a1 = JSONArray.fromObject(jso1.get("tanks"));
		int tankssize = a1.size();
		for (int i = 0; i < a1.size(); i++) {
			if (tanks.size() < i+1) {
				Tank tmp = new Tank(0,0,false);
				tanks.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			Tank t = tanks.get(i); // 获得键值对的键
			t.setdraws(aa1, true);
			t.draw(g);
		}

		a1 = JSONArray.fromObject(jso1.get("river"));
		for (int i = 0; i < a1.size(); i++) {
			if (theRiver.size() < i+1) {
				River tmp = new River(0,0,this);
				theRiver.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			River r = theRiver.get(i); // 每一个坦克撞到河流时
			r.setdraw(aa1);
			r.draw(g);
		}
		
		aa1 = JSONArray.fromObject(jso1.get("blood"));
		blood.setdraw(aa1);
		blood.draw(g);

		a1 = JSONArray.fromObject(jso1.get("trees"));
		for (int i = 0; i < a1.size(); i++) { // 画出trees
			if (otherWall.size() < i+1) {
				Tree tmp = new Tree(0,0,this);
				trees.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			Tree tr = trees.get(i);
			tr.setdraw(aa1);
			tr.draw(g);
		}


		a1 = JSONArray.fromObject(jso1.get("otherWall"));
		for (int i = 0; i < a1.size(); i++) {
			if (otherWall.size() < i+1) {
				CommonWall tmp = new CommonWall(0,0,this);
				otherWall.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			CommonWall cw = otherWall.get(i);
			cw.setdraw(aa1);
			cw.draw(g);
		}

		a1 = JSONArray.fromObject(jso1.get("homeWall"));
		for (int i = 0; i < a1.size(); i++) { // 家里的坦克撞到自己家
			if (homeWall.size() < i+1) {
				CommonWall tmp = new CommonWall(0,0,this);
				homeWall.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			CommonWall cw = homeWall.get(i);
			cw.setdraw(aa1);
			cw.draw(g);
		}
		
		a1 = JSONArray.fromObject(jso1.get("metalWall"));
		for (int i = 0; i < a1.size(); i++) { // 家里的坦克撞到自己家
			if (metalWall.size() < i+1) {
				MetalWall tmp = new MetalWall(0,0,this);
				metalWall.add(tmp);
			}
			aa1 = JSONArray.fromObject(a1.get(i));
			MetalWall mw = metalWall.get(i);
			mw.setdraw(aa1);
			mw.draw(g);
		}
		
		a1 = JSONArray.fromObject(jso1.get("bombTanks"));
		for (int i = this.havereadbomb; i < a1.size(); i++) { // 画出爆炸效果
			BombTank tmp = new BombTank(0,0,this);
			aa1 = JSONArray.fromObject(a1.get(i));
			tmp.setdraw(aa1);
			bombTanks.add(tmp);
		} 
		countbomb += a1.size()-this.havereadbomb;
		this.havereadbomb = a1.size();
		System.out.println("a1.size:"+a1.size());
		for (int i = 0; i < bombTanks.size(); i++) {
			bombTanks.get(i).draw(g);
		}
		
		g.setColor(Color.green); // 设置字体显示属性
		Font f1 = g.getFont();
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("区域内还有敌方坦克: ", 100, 70);
		g.setFont(new Font("TimesRoman", Font.ITALIC, 20));
		g.drawString("" + tankssize, 300, 70);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("player 1剩余生命值: ", 350, 70);
		g.drawString("player 2剩余生命值: ", 350, 90);
		g.drawString("分数: ", 600, 70);
		g.drawString("分数: ", 600, 90);
		g.setFont(new Font("TimesRoman", Font.ITALIC, 20));
		g.drawString("" + homeTank.getLife(), 550, 70);
		g.drawString("" + homeTank2.getLife(), 550, 90);
		g.drawString("" + homeTank.score, 660, 70);
		g.drawString("" + homeTank2.score, 660, 90);
		g.setFont(f1);
		

		}catch(Exception e) { System.out.println("Error!");}
	}

	public TankClient(int player, String nickname, String address) {
		this.player = player;
		
		
		this.nickname = nickname;
		this.address = address;
		t1 = new TCPClientin(this, player, this.address, nickname);
		t1.start();
		System.out.println(jsstring);
		// printable = false;
		// 创建菜单及菜单选项
		jmb = new MenuBar();
		jm1 = new Menu("游戏");
		jm2 = new Menu("暂停/继续");
		jm3 = new Menu("帮助");
		jm4 = new Menu("游戏级别");
		jm1.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
		jm2.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
		jm3.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
		jm4.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体

		jmi1 = new MenuItem("开始新游戏");
		jmi2 = new MenuItem("退出");
		jmi3 = new MenuItem("暂停");
		jmi4 = new MenuItem("继续");
		jmi5 = new MenuItem("游戏说明");
		jmi6 = new MenuItem("级别1");
		jmi7 = new MenuItem("级别2");
		jmi8 = new MenuItem("级别3");
		jmi9 = new MenuItem("级别4");
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

		jmi1.addActionListener(this);
		jmi1.setActionCommand("NewGame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("Exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("Stop");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("Continue");
		jmi5.addActionListener(this);
		jmi5.setActionCommand("help");
		jmi6.addActionListener(this);
		jmi6.setActionCommand("level1");
		jmi7.addActionListener(this);
		jmi7.setActionCommand("level2");
		jmi8.addActionListener(this);
		jmi8.setActionCommand("level3");
		jmi9.addActionListener(this);
		jmi9.setActionCommand("level4");

		this.setMenuBar(jmb);// 菜单Bar放到JFrame上
		this.setVisible(true);

		
		for (int i = 0; i < 10; i++) { // 家的格局
			if (i < 4)
				homeWall.add(new CommonWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

		}

		for (int i = 0; i < 32; i++) {
			if (i < 16) {
				otherWall.add(new CommonWall(220 + 20 * i, 300, this)); // 普通墙布局
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

		for (int i = 0; i < 20; i++) { // 金属墙布局
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));
			else
				metalWall.add(new MetalWall(500 + 30 * (i - 10), 160, this));
		}

		for (int i = 0; i < 4; i++) { // 树的布局
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(220 + 30 * i, 360, this));
				trees.add(new Tree(440 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}

		}

		theRiver.add(new River(85, 100, this));

		for (int i = 0; i < 20; i++) { // 初始化20辆坦克
			if (i < 9) // 设置坦克出现的位置
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this, false));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
						this, false));
			else
				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D,
								this, true));
		}

		this.setSize(Fram_width, Fram_length); // 设置界面大小
		this.setLocation(280, 50); // 设置界面出现的位置
		if (this.player == 1) {
			this.setTitle("坦克大战双人版玩家1（蓝色坦克）");
		} else {
			this.setTitle("坦克大战双人版玩家2（黄色坦克）");
		}

		this.addWindowListener(new WindowAdapter() { // 窗口监听关闭
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.setVisible(true);

		JOptionPane.showMessageDialog(null, "点击确定等待开始游戏",
				"提示！", JOptionPane.INFORMATION_MESSAGE);
		new TCPClientout(997+this.player, this.address).start(); //998,999等待开始游戏
		this.addKeyListener(new KeyMonitor(this.player, this.address));// 键盘监听
		new Thread(new bgMusicV2()).start();
		new Thread(new PaintThread()).start(); // 线程启动
	}

	public void init() { //恢复初始设置
		//printable = false;
		this.bombTanks.clear();
		this.havereadbomb = 0;
		homeWall.clear();
		for (int i = 0; i < 10; i++) { // 家的格局
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
				otherWall.add(new CommonWall(220 + 20 * i, 300, this)); // 普通墙布局
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
		for (int i = 0; i < 20; i++) { // 金属墙布局
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));
			else
				metalWall.add(new MetalWall(500 + 30 * (i - 10), 160, this));
		}

		trees.clear();
		for (int i = 0; i < 4; i++) { // 树的布局
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
		for (int i = 0; i < 20; i++) { // 初始化20辆坦克
			if (i < 9) // 设置坦克出现的位置
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
		String address;
		int player;
		KeyMonitor(int player, String address) {
			this.player = player;
			this.address = address;	
		}
		public void keyReleased(KeyEvent e) { // 监听键盘释放
			if (this.player == 1) {
				int hr1 = homeTank.keyReleasedforclient(e);
				if (hr1%10 != 5) {
					TCPClientout thread = new TCPClientout(hr1, this.address);
					thread.start();
				}
			} else {
				int hr2 = homeTank.keyReleased2forclient(e);
				if (hr2%10 != 5) {
					TCPClientout thread = new TCPClientout(hr2, this.address);
					thread.start();
				}
			}
			
		}

		public void keyPressed(KeyEvent e) { // 监听键盘按下
			if (this.player == 1) {
				int hp1 = homeTank.keyPressedforclient(e);
				if (hp1%10 != 5) {
					TCPClientout thread = new TCPClientout(hp1, this.address);
					thread.start();
				}
			} else {
				int hp2 = homeTank.keyPressed2forclient(e);
				if (hp2%10 != 5) {
					TCPClientout thread = new TCPClientout(hp2, this.address);
					thread.start();
				}
			}
			
		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("NewGame")) {
			TCPClientout thread = new TCPClientout(1000, this.address); //1000表示stop
			thread.start();
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要开始新游戏！", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				new TCPClientout(1002, this.address).start(); //1002表示重开
				this.dispose();
				new TankClient(this.player, this.nickname, this.address);
			} else {
				TCPClientout threads = new TCPClientout(1001, this.address);
				threads.start();
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			TCPClientout thread = new TCPClientout(1000, this.address); //1000表示stop
			thread.start();
			//printable = false;
			// try {
			// Thread.sleep(10000);
			//
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		} else if (e.getActionCommand().equals("Continue")) {
			TCPClientout thread = new TCPClientout(1001, this.address); //1001表示continue
			thread.start();
			//if (!printable) {
				//printable = true;
				//new Thread(new PaintThread()).start(); // 线程启动
			//}
			// System.out.println("继续");
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("退出");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}

		} else if (e.getActionCommand().equals("help")) {
			TCPClientout thread = new TCPClientout(1000, this.address);
			thread.start();
			//printable = false;
			JOptionPane.showMessageDialog(null, "玩家1通过w,s,a,d控制坦克的上下左右移动，通过f控制坦克射击；玩家2通过↑,↓,←,→控制坦克的上下左右移动，通过空格(space)控制坦克射击",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
			TCPClientout threads = new TCPClientout(1001, this.address);
			threads.start();
			//this.setVisible(true);
			//printable = true;
			//new Thread(new PaintThread()).start(); // 线程启动
		} else if (e.getActionCommand().equals("level1")) {
			new TCPClientout(1000, this.address).start();
			new TCPClientout(1011, this.address).start();; //1011表示level
			Tank.count = 12;
			Tank.speedX = 6;
			Tank.speedY = 6;
			Bullets.speedX = 10;
			Bullets.speedY = 10;
			this.dispose();
			new TankClient(this.player, this.nickname, this.address);
		} else if (e.getActionCommand().equals("level2")) {
			new TCPClientout(1000, this.address).start();
			new TCPClientout(1012, this.address).start();; //1012表示leve2
			Tank.count = 12;
			Tank.speedX = 10;
			Tank.speedY = 10;
			Bullets.speedX = 12;
			Bullets.speedY = 12;
			this.dispose();
			new TankClient(this.player, this.nickname, this.address);

		} else if (e.getActionCommand().equals("level3")) {
			new TCPClientout(1000, this.address).start();
			new TCPClientout(1013, this.address).start();; //1013表示leve3
			Tank.count = 20;
			Tank.speedX = 14;
			Tank.speedY = 14;
			Bullets.speedX = 16;
			Bullets.speedY = 16;
			this.dispose();
			new TankClient(this.player, this.nickname, this.address);
			
		} else if (e.getActionCommand().equals("level4")) {
			new TCPClientout(1000, this.address).start();
			new TCPClientout(1014, this.address).start();; //1014表示leve4
			Tank.count = 20;
			Tank.speedX = 16;
			Tank.speedY = 16;
			Bullets.speedX = 18;
			Bullets.speedY = 18;
			this.dispose();
			new TankClient(this.player, this.nickname, this.address);
		}
	}
	String TransiJSON() {
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
		for (int j = 0; j < otherWall.size(); j++) {// 每一个子弹打到其他墙上
			CommonWall w = otherWall.get(j);
			aa1 = new ArrayList<Integer>();
			aa1.add(w.x);
			aa1.add(w.y);
			a1.add(aa1);
		}
		jso1.put("otherWall", a1); 
		
		a1 = new ArrayList<ArrayList>();
		for (int j = 0; j < homeWall.size(); j++) {// 每一个子弹打到家的墙上
			CommonWall cw = homeWall.get(j);
			aa1 = new ArrayList<Integer>();
			aa1.add(cw.x);
			aa1.add(cw.y);
			a1.add(aa1);
		}
		jso1.put("homeWall", a1); 
		
		a1 = new ArrayList<ArrayList>();
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i); // 获得键值对的键
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
		for (int i = 0; i < bombTanks.size(); i++) { // 画出爆炸效果
			BombTank bt = bombTanks.get(i);
			aa1 = new ArrayList<Integer>();
			aa1.add(bt.step);
			aa1.add(bt.x);
			aa1.add(bt.y);
			a1.add(aa1);
		}
		jso1.put("bombTanks", a1);
		
		System.out.println(jso1.toString());
		return jso1.toString();
	}
	

	
	public static void main(String[] args) {
		StartFrame sf1 = new StartFrame();
		while (!sf1.isstarted) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		new TankClient(2, sf1.nickname, sf1.address); // 实例化
		//new TankClient(2, "dog", sf1.address); // 实例化
		//new Thread(new bgMusic2V2()).start();

	}
}
