package com.hu.springboot.controller;

import com.hu.springboot.entity.User;
import com.hu.springboot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class UserController {




    @Autowired
    UserRepository userRepository;

    @Resource
    protected HttpServletRequest request;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {

        User user = userRepository.findById(id).get();

        logger.info("姓名:" + user.getLastName());
        return user;
    }

    @GetMapping("/user")
    public User insertUser(User user) {
        User save = userRepository.save(user);
        return save;
    }


    @RequestMapping(value = "yth", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Map<String, Object> getString() throws UnsupportedEncodingException, IOException {

        //后台接收
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) { // 读取数据
            sb.append(line + "\n");
        }
        br.close();
        logger.info("请求json:" + sb.toString());


        //User user = userRepository.findById(1).get();
        User user = userRepository.findAll().get(1);
        //响应
        LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();  //创建Json对象
        jsonObject.put("username", user.getLastName());         //设置Json对象的属性
        jsonObject.put("email", user.getEmail());
        return jsonObject;
    }


}

