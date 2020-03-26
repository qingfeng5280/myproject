package com.hu.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/***
 *
 * @Description: 摘要加密算法辅助类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年05月29日
 */
public class DigestHelper {
	/***
	 * 获取请求签名值
	 *
	 * @param data
	 *            加密前数据
	 * @param key
	 *            密钥
	 * @param algorithm
	 *            HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
	 * @param encoding
	 *            编码格式
	 * @return HMAC加密后16进制字符串
	 * @throws Exception
	 */
	public static String getSignature(String data, String key) {
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
			mac.init(secretKey);
			mac.update(data.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("获取Signature签名信息异常：" + e.getMessage());
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("获取Signature签名信息异常：" + e.getMessage());
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			System.out.println("获取Signature签名信息异常：" + e.getMessage());
			return null;
		}
		return byte2hex(mac.doFinal());
	}

	/***
	 * 将byte[]转成16进制字符串
	 *
	 * @param data
	 *
	 * @return 16进制字符串
	 */
	public static String byte2hex(byte[] data) {
		StringBuilder hash = new StringBuilder();
		String stmp;
		for (int n = 0; data != null && n < data.length; n++) {
			stmp = Integer.toHexString(data[n] & 0XFF);
			if (stmp.length() == 1)
				hash.append('0');
			hash.append(stmp);
		}
		return hash.toString();
	}
}
