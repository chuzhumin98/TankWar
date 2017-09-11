import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

//设置界面树和丛林
public class Tree {
	public static final int width = 30;
	public static final int length = 30;
	int x, y;
	TankServer tc ;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] treeImags = null;
	static {
		treeImags = new Image[]{
				tk.getImage(CommonWall.class.getResource("Images/tree.gif")),
		};
	}
	
	
	public Tree(int x, int y, TankServer tc) {  //Tree的构造方法，传递x，y和tc对象
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {           //画出树
		g.drawImage(treeImags[0],x, y, null);
	}
	
}
