package com.hu;

import com.hu.springboot.dao.UserDoMapper;
import com.hu.springboot.dataobject.UserDo;
import com.hu.utils.HttpUtils;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication(scanBasePackages = {"com.hu.springboot"})
@RestController
@MapperScan("com.hu.springboot.dao")
public class MyProjectApplication {

    @Autowired
    private UserDoMapper userDoMapper ;

    @RequestMapping("/")
    public  String hello(){
        UserDo userDO = userDoMapper.selectByPrimaryKey(1) ;
        if(userDO == null){
            return "用户对象不存在" ;
        }else {
            return userDO.getLastName();

        }
    }

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
