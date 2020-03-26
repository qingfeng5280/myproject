package com.hu;

import com.hu.utils.HttpUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class MyProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProjectApplication.class, args);



        //请求json
 /*       HashMap<String, String> maps = new HashMap<String, String>();
        maps.put("type", "02");
        maps.put("chb140", "1800791");
        maps.put("result", "成功");

        String vo = HttpUtils.doPost("http://localhost:8080/yth", maps);
        System.out.println("++++++++++调用结果+++++++++++++++"+vo);*/
    }
}
