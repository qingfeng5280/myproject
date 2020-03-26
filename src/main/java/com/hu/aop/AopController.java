package com.hu.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AopController {

    @RequestMapping(value = "/hello11")
    public String sayHello(){
        System.out.println("AOP方法执行……");

        //int a = 1/0 ;
        return "hello " ;
    }
}