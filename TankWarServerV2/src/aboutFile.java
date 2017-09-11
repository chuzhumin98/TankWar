import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class aboutFile {
	String filename = "logs.txt";
	ArrayList<String> as = new ArrayList<String>();
	ArrayList<Integer> ai = new ArrayList<Integer>();
	ArrayList<String> top5s = new ArrayList<String>();
	ArrayList<String> top5i = new ArrayList<String>();
	URL u1 = BombTank.class.getClassLoader().getResource("log/logs.txt");
	String path = System.getProperty("java.class.path");
	void run(Graphics g, String[] nicknames, int[] scores) {
		System.out.println(path);
	   	BufferedWriter out;
		try {
			FileWriter f = new FileWriter(filename, true);
			out = new BufferedWriter(f);
			for (int i = 0; i < nicknames.length; i++) {
				out.write(nicknames[i]);
				out.write(" ");
				String stmp = String.valueOf(scores[i]);
				out.write(stmp);
				out.newLine();
			}			
			out.close();
			System.out.println("succeed written");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			InputStream ism=u1.openStream(); 
			//BufferedReader buffer = new BufferedReader(ism);
			Scanner input = new Scanner(new File(filename));
			as.clear();
			ai.clear();
			top5s.clear();
			top5i.clear();
			while (true) {
				if (input.hasNext()) {
					as.add(input.next());
				} else {
					break;
				}
				if (input.hasNextInt()) {
					ai.add(input.nextInt());
				} else {
					break;
				}
			}
			for (int i = 0; i < ai.size(); i++) {
				System.out.println(as.get(i)+" "+ai.get(i));
			}
			g.setColor(Color.green); // 设置字体显示属性
			g.setFont(new Font("TimesRoman", Font.BOLD, 35)); 
			g.drawString("Top 5 ", 350, 200);
			g.setFont(new Font("TimesRoman", Font.BOLD, 25)); 
			for (int i = 0; i < 5; i++) {
				int index = getMaxindex();
				if (index == -1) {
					break;
				}
				String itmp = String.valueOf(ai.get(index));
				g.drawString(as.get(index), 300, 260+i*30);
				g.drawString(itmp, 395, 260+i*30);
				top5s.add(as.get(index));
				top5i.add(itmp);
				as.remove(index);
				ai.remove(index);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	void showprint(Graphics g) { //只显示，不输出
		try {
			Scanner input = new Scanner(new File(filename));
			as.clear();
			ai.clear();
			top5s.clear();
			top5i.clear();
			while (true) {
				if (input.hasNext()) {
					as.add(input.next());
				} else {
					break;
				}
				if (input.hasNextInt()) {
					ai.add(input.nextInt());
				} else {
					break;
				}
			}
			for (int i = 0; i < ai.size(); i++) {
				System.out.println(as.get(i)+" "+ai.get(i));
			}
			g.setColor(Color.green); // 设置字体显示属性
			g.setFont(new Font("TimesRoman", Font.BOLD, 35)); 
			g.drawString("Top 5 ", 350, 200);
			g.setFont(new Font("TimesRoman", Font.BOLD, 25)); 
			for (int i = 0; i < 5; i++) {
				int index = getMaxindex();
				if (index == -1) {
					break;
				}
				String itmp = String.valueOf(ai.get(index));
				g.drawString(as.get(index), 300, 260+i*30);
				g.drawString(itmp, 395, 260+i*30);
				top5s.add(as.get(index));
				top5i.add(itmp);
				as.remove(index);
				ai.remove(index);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	int getMaxindex() { //如果没有元素，则返回-1
		if (ai.size() == 0) {
			return -1;
		}
		int index = 0;
		for (int i = 1; i < ai.size(); i++) {
			if (ai.get(i) > ai.get(index)) { //优先让旧的高分排在前面
				index = i;
			}
		}
		return index;
	}
//	public static void main(String[] args) {
//		String[] stmp = {"cat"};
//		int[] itmp = {6};
//		new aboutFile().run(stmp, itmp);
//	}
}
