package top.naccl.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: ip记录
 * @Author: Naccl
 * @Date: 2020-05-14
 */

public class IpAddressUtils {
	private static RestTemplate restTemplate = new RestTemplate();
	private static String KEY = "腾讯API的密钥";
	private static String apiURI = "https://apis.map.qq.com/ws/location/v1/ip?ip=";

	/**
	 * 在Nginx等代理之后获取用户真实IP地址
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		}
		return ip;
	}

	public static JSONObject getCityInfo(HttpServletRequest request) {
		JSONObject resJSON = getAPIResult(getIpAddress(request));
		int status = (int) resJSON.get("status");
		if (status == 0) {
			JSONObject result = resJSON.getJSONObject("result");
			return result;
		} else {
			System.err.println("状态码错误");
			return null;
		}
	}

	private static JSONObject getAPIResult(String ip) {
		StringBuilder apiURL = new StringBuilder(apiURI).append(ip).append("&key=").append(KEY);
		ResponseEntity responseEntity = restTemplate.getForEntity(apiURL.toString(), JSONObject.class);
		System.out.println(responseEntity.getBody());
		return (JSONObject) responseEntity.getBody();
	}
}


