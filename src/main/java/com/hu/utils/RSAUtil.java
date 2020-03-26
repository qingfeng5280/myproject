package com.hu.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RSA签名工具类
 * Created by 刘志强 on 2017/4/24.
 */
public class RSAUtil {

    /**
     * 日志
     */
    private static final Logger logger                    = LoggerFactory.getLogger(RSAUtil.class);

    private static final int    DEFAULT_BUFFER_SIZE       = 8192;

    private static final String SIGN_CHARSET              = "UTF-8";

    private static final String SIGN_TYPE_RSA             = "RSA";

    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    private static final String PRIVATE_KEY               = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDUzUVSMF5cvy3Q\n" +
            "3Ilup0Hdu56juwV4LWDH2t5u4HDgbRYNNsBQtxfp4dz6sl6qw+OnQlk7wlbt79Rl\n" +
            "qi7BRa3x2CgqzKxTZlQ3vLwojnEquwE2k9fPpi3p6pDhmR2UQzoSdZrsrn9dUBgk\n" +
            "6FzvFnooUWkign1RCwDOMDWmF0yisos1YzNYg5LCOLypEZx0L7SDbQNDisqz5QeV\n" +
            "XzCgKTSD46DvcH5vnluKa+yp9WAYqsIv7yfK/TrvqCgpqvEkyTJZQe3/jWlMfNap\n" +
            "QAv8Ec72zG/L/VOT0yPd7gAE5LZm6pMU1M0lYlNdEfaqsxl6lqa8SNOGinC9sJnY\n" +
            "r3NqtwIpAgMBAAECggEAObuG9wlQUHE9iNCMKI0P/YwNOfZfQX9uJkpm1kXl2b0+\n" +
            "ok8cVcmWn0k7nngN3t5OQO5rLx9GXj8WrI5DKQQycM5n91SX2/iDGKwHkCk04pbh\n" +
            "eWEax6caBcAxlkLoF3U1qBRn95ZPz40PP9QagnLiqVWM5GPqg7YsfcPuiiUAEZNI\n" +
            "Dz5cp2j3clupld6IBUP16b2HSi2zU0LoFWlGpqmPppNcr/yYsq9PZ3frTnHmB48W\n" +
            "CWoVyDiSUM9PQUsLCiRUGMmT56lUIjJq+dQAchMOkLs9uU0XJ6a7Hsnq+pgxrn34\n" +
            "PySsiB3+6Ccosp+QsmJan1e+KHQd9w5RgVDBsGIIQQKBgQD0x3AvRLpHch4stBg1\n" +
            "wTEaYpJLmfkaTxNKd7XrbbT0gZwfKuiGEicTQCgTZe2reFBIC0EWONcWMhSaj9HF\n" +
            "PAW8XHwL1U/7PKLMbiBbgWLgE0LPED4G5V5tLi6fBAvN/02UYN2EmC2Iy6INJryy\n" +
            "RWuXr3G7jHYIALgJynD1WEyOtQKBgQDejpHM/jkn5y64eu7zHQXtnQhQ68lcEGno\n" +
            "iUhXOXo01XyAvYs+PErZJ1Dh3uE6DEQqbXm6dRadUel7DCMO7EkovUgXP2qV7Ee8\n" +
            "w90jDxttxNCL/f79mIsiiTP6mI8DJY8w2a0NC/06cKMZlG4l/AzAH6OVhr8Fk0UJ\n" +
            "smibbYgaJQKBgEPMGZ5+ArpXOVRYbXw2Dkuokayu8PgAfu9ZSOH0ePJgE8XuNIkt\n" +
            "7fS/CFqXGMUthc7ujxCAndJf/KTywL9IaK5LT3BXnNeRa+YDeqLdq2006IRBk2yF\n" +
            "iyniaLdSf5KG7A305mYbYEFLZY8O86TM5YgV6AJyXR7KVC8iCzdpRYshAoGBANrG\n" +
            "+7ywDC+Vqu/eqRaD6VfzjkE+B+tglzvn+B0Ge4OEead0rHS5gDqH05K2LmGJvViX\n" +
            "1ZndkCWGaCZ1f+Ejladeqodv568JXlvJjpDwQN5fI0L6pLdWRS/mABrP7YYCXgh/\n" +
            "cwXoSz4vi6WRq9XbOA2FDKHom883Ph6SKxLfUXOhAoGAc764pV4d+oqoV3xNta7f\n" +
            "xGOvPxUfoJ1n1VF2y8mMTuoJiSBzyXRj4gCXiMK2OzrrVpm4tMWl1NzYVZyuPopw\n" +
            "P89Bnv7gUENa9487k+5vMJWj/kTfVjHbMcTsfS2DDPZtohl0UHNHINCEXLS80LN9\n" +
            "IBsc1MBReXbJ+x5sWCDjRCE=";

    private static final String PUBLIC_KEY                = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjGpnmKByseFngibUJiNIc4/6wfIQHMOHAsUNe1EACJUrFDFat78IqCJZIsYZ91G8He+88rBQL9+zK7DoubJsHPb6JTHE8krdFN1u/oaEDQz/k5YNff89byrAdjsa3g5GcRU4d1/1D1TpkSVk0BTojtAgCgyNfRPqxB95gF+Bsu2LQB1qMojSVoUx7rbSMofexahjqCWBqIryDYskegjNZhcfh0fiIZd/hK7bZDILN+gHhi60H5vvAEe9M84GYo4yszW51qt8Blf2u5SZ0WkiAsh4+AMFdoRpBDP7MGsmWIYLkylSYy2QoSM4nyaEFZXp3qk/zI9k2HSB/XuW8FwFXQIDAQAB";

    /**
     * RSA2签名
     *
     * @param map 待签名数据和签名字段字符串
     * @return 签名
     * @throws Exception 异常上层处理
     */
    public static String rsaSign(Map<String, String> map) throws Exception {
        String content = getSignContent(map);
        String reqSeq = map.get("reqSeq");
        logger.info(">>待签名数据为:" + reqSeq + "," + content);
        PrivateKey priKey = getPrivateKeyFromPKCS8(
            new ByteArrayInputStream(PRIVATE_KEY.getBytes()));
        Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes(SIGN_CHARSET));
        String sign = new String(Base64.encodeBase64(signature.sign()));
        logger.info(">>签名为:" + reqSeq + "," + sign);
        return sign;
    }

    /**
     * RSA2验签
     *
     * @param map 待验签数据、签名信息和签名字段字符串
     * @return 验签结果 true/false
     * @throws Exception 异常上层处理
     */
    public static boolean rsaCheck(Map<String, String> map) throws Exception {
        String content = getSignContent(map);
        String reqSeq = map.get("reqSeq");
        String sign = map.get("sign");
        logger.info(">>待验证的签名为:" + reqSeq + "," + sign);
        logger.info(">>生成签名的参数为:" + reqSeq + "," + content);
        PublicKey pubKey = getPublicKeyFromX509(new ByteArrayInputStream(PUBLIC_KEY.getBytes()));
        Signature signature = Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        signature.initVerify(pubKey);
        signature.update(content.getBytes(SIGN_CHARSET));
        boolean signResult = signature.verify(Base64.decodeBase64(sign.getBytes()));
        logger.info(">>验签结果:" + reqSeq + "," + signResult);
        return signResult;
    }

    /**
     * 把参数按照ASCII码递增排序，如果遇到相同字符则按照第二个字符的键值ASCII码递增排序
     * 将排序后的参数与其对应值，组合成“参数=参数值”的格式，并且把这些参数用&字符连接起来
     *
     * @param sortedParams 待签名数据和签名字段字符串
     * @return 待签名字符串
     */
    private static String getSignContent(Map<String, String> sortedParams) {
        //appId,method,bizContent,生成签名所需的参数
        String[] sign_param = sortedParams.get("sign_param").split(",");
        List<String> keys = new ArrayList<>();
        Collections.addAll(keys, sign_param);
        Collections.sort(keys);
        StringBuilder content = new StringBuilder();
        int index = 0;
        for (String key : keys) {
            String value = sortedParams.get(key);
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }

    private static PrivateKey getPrivateKeyFromPKCS8(InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        byte[] encodedKey = readText(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    private static PublicKey getPublicKeyFromX509(InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    private static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();
        io(reader, writer, -1);
        return writer.toString();
    }

    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        }
        char[] buffer = new char[bufferSize];
        int amount;
        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
    
	public static String getNewFjcsn(String fjcsn){
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(fjcsn.charAt(6)).append(fjcsn.charAt(7)).append(fjcsn.charAt(4)).append(fjcsn.charAt(5)).append(fjcsn.charAt(2)).append(fjcsn.charAt(3)).append(fjcsn.charAt(0)).append(fjcsn.charAt(1));
    	
    	return sb.toString();
    }
}
