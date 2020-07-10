package com.hu.springboot.controller;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

/**
 * 此controller类用作各种测试用
 */

@RestController
@Slf4j
public class TestController {

    /**
     * 测试使用hutool工具包的HttpUtil，有了hutool工具包就可以带地本地的utils包
     */
    @GetMapping(value = "/getinfo")
    public void getinfo(){
        //请求json
        HashMap<String, Object> maps = new HashMap<String, Object>();
        maps.put("type", "02");
        maps.put("chb140", "1800791");
        maps.put("result", "成功");

        String vo = HttpUtil.post("http://localhost:8080/yth", maps);
        log.info("++++++++++调用结果+++++++++++++++"+vo);
    }

}
