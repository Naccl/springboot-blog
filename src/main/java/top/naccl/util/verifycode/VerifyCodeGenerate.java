package top.naccl.util.verifycode;

import top.naccl.util.RandomUtils;

import java.awt.*;
import java.util.Random;

public interface VerifyCodeGenerate {
	static final String[] FONT_TYPES = {"宋体", "新宋体", "黑体", "楷体", "隶书"};

	VerifyCode generate();

	/**
	 * 设置背景颜色及大小，干扰线
	 */
	default void fillBackground(Graphics graphics, int width, int height) {
		// 填充背景
		graphics.setColor(Color.WHITE);
		//设置矩形坐标x y 为0
		graphics.fillRect(0, 0, width, height);
		// 加入干扰线条
		for (int i = 0; i < 8; i++) {
			//设置随机颜色算法参数
			graphics.setColor(RandomUtils.randomColor(40, 150));
			Random random = new Random();
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			graphics.drawLine(x, y, x1, y1);
		}
	}

	/**
	 * 设置字符颜色大小
	 */
	default void createCharacter(Graphics g, String randomStr) {
		char[] charArray = randomStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			//设置RGB颜色算法参数
			g.setColor(new Color(50 + RandomUtils.nextInt(100), 50 + RandomUtils.nextInt(100), 50 + RandomUtils.nextInt(100)));
			//设置字体大小，类型
			g.setFont(new Font(FONT_TYPES[RandomUtils.nextInt(FONT_TYPES.length)], Font.BOLD, 26));
			//设置x y 坐标
			g.drawString(String.valueOf(charArray[i]), 15 * i + 5, 19 + RandomUtils.nextInt(8));
		}
	}
}
