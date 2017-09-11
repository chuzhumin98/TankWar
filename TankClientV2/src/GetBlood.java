import java.awt.*;
import java.util.Random;

import net.sf.json.JSONArray;

public class GetBlood {
	
	public static final int width = 36;
	public static final int length = 36;

	int x, y;
	TankClient tc;
	private static Random r = new Random();

	int step = 0; 
	boolean live = false;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] bloodImags = null;
	static {
		bloodImags = new Image[] { tk.getImage(CommonWall.class
				.getResource("Images/hp.png")), };
	}

	private int[][] poition = { { 155, 196 }, { 500, 58 }, { 80, 340 },
			{ 99, 199 }, { 345, 456 }, { 123, 321 }, { 258, 413 } };

	void setdraw(JSONArray aa1) {
		if (aa1.getInt(0) == 1) {
			live = true;
		}  else {
			live = false;
		}
		this.x = aa1.getInt(1);
		this.y = aa1.getInt(2);
	}
	public void draw(Graphics g) {
		if (!live)
			return;
		g.drawImage(bloodImags[0], x, y, null);

	}

	private void move() {
		step++;
		if (step == poition.length) {
			step = 0;
		}
		x = poition[step][0];
		y = poition[step][1];
		
	}

	public Rectangle getRect() { //返回长方形实例
		return new Rectangle(x, y, width, length);
	}

	public boolean isLive() {//判断是否还活着
		return live;
	}

	public void setLive(boolean live) {  //设置生命
		this.live = live;
	}

}
