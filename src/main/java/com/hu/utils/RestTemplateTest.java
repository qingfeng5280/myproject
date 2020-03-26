package com.hu.utils;

/**
 * CitizenCard.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;




//import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.itextpdf.text.List;
//import com.smk.BaseApplication;

import net.sf.json.JSONObject;

import javax.net.ssl.HostnameVerifier;

/**
 * RestTemplate 测试类
 * @author liuzq
 * @version $Id: RestTemplateTest.java, v 0.1 2017年3月31日 下午2:32:32 liuzq Exp $
 */


public class RestTemplateTest {

 public static void main(String[] args) throws Exception {
  RestTemplate restTemplate = new RestTemplate();
  HttpHeaders headers = new HttpHeaders();
  headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
  //headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
  Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  String reqSeq = System.currentTimeMillis() + "";
  //请求接口实际数据
  Map<String, Object> messageMap = new HashMap<>();

  messageMap.put("operatorId", "_9000000");
  messageMap.put("channelId", "0005");
  messageMap.put("posId", "qrCode");

  messageMap.put("qrCode", "9200000002541825188505");
  String messageMapStr = gson.toJson(messageMap);
  //请求开放平台数据
  Map<String, String> openMap = new HashMap<>();
  openMap.put("reqSeq", reqSeq);
  openMap.put("appId", "HZRS0005");
  openMap.put("method", "queryQrCode");
  openMap.put("bizContent", messageMapStr);
  openMap.put("sign_param", "appId,method,bizContent");
  //加签
  openMap.put("sign", RSAUtil.rsaSign(openMap));
  String message = gson.toJson(openMap);
  HttpEntity<String> entity = new HttpEntity<String>(message, headers);
  String urlstr = "https://172.16.69.140:18088/openapi";


  ResponseEntity<String> resp = restTemplate.postForEntity(urlstr, entity, String.class);
  String result = resp.getBody();
  JSONObject json = JSONObject.fromObject(result);
  System.out.println(json);
  //验签
  /*      Map<String, String> checkMap = new HashMap<>();
        checkMap.put("reqSeq", reqSeq);
        checkMap.put("sign_param", "success,value");
        checkMap.put("success", json.getString("success"));
        checkMap.put("value", json.getString("value"));
        checkMap.put("sign", json.getString("sign"));
        System.out.println(RSAUtil.rsaCheck(checkMap));*/
 }

}

