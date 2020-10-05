package top.naccl.util;

import org.springframework.util.DigestUtils;

/**
 * @Description: MD5加密
 * @Author: Naccl
 * @Date: 2020-04-24
 */
public class MD5Utils {

	public static String getMD5(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}

	public static void main(String[] args) {

	}
}