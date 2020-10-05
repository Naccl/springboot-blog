package top.naccl.util;

import com.alibaba.fastjson.JSONObject;

import java.awt.*;
import java.util.Random;

/**
 * @Description: 随机数工具类
 * @Author: Naccl
 * @Date: 2020-05-09
 */
public class RandomUtils {
	private static final char[] CODE_SEQ = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
			'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

	private static final char[] NUMBER_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	private static Random random = new Random();

	public static int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(CODE_SEQ[nextInt(CODE_SEQ.length)]);
		}
		return sb.toString();
	}

	public static String randomNumberString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(NUMBER_ARRAY[nextInt(NUMBER_ARRAY.length)]);
		}
		return sb.toString();
	}

	/**
	 * 简单数字运算字符串：20-3=?、16-5=?
	 *
	 * @param numBound1 第一个数范围
	 * @param numBound2 第二个数范围，第一个数范围应比第二个数范围大 [10,20] > [0,9]
	 * @param sign      运算符号
	 * @return
	 */
	public static JSONObject randomSimpleMathString(int[] numBound1, int[] numBound2, char sign) {
		StringBuilder sb = new StringBuilder();
		int num1 = nextInt(numBound1[1] - numBound1[0]) + numBound1[0];
		int num2 = nextInt(numBound2[1] - numBound2[0]) + numBound2[0];
		String strNum1 = String.valueOf(num1);
		String strNum2 = String.valueOf(num2);
		sb.append(strNum1);
		int answer;
		if (sign == '-') {
			sb.append('-');
			answer = num1 - num2;
		} else {
			sb.append('+');
			answer = num1 + num2;
		}
		sb.append(strNum2);
		sb.append("=?");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("num1", num1);
		jsonObject.put("num2", num2);
		jsonObject.put("answer", answer);
		jsonObject.put("string", sb.toString());
		return jsonObject;
	}

	public static Color randomColor(int f, int b) {
		if (f > 255) {
			f = 255;
		}
		if (b > 255) {
			b = 255;
		}
		return new Color(f + nextInt(b - f), f + nextInt(b - f), f + nextInt(b - f));
	}
}
