package com.hu.springboot.controller;

import com.hu.springboot.error.BusinessException;
import com.hu.springboot.error.EmBusinessError;
import com.hu.springboot.response.CommonReturnType;
import com.hu.springboot.service.OrderService;
import com.hu.springboot.service.model.OrderModel;
import com.hu.springboot.service.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@Slf4j
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    RedisTemplate redisTemplate;
    
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,
                                        @RequestParam(name="amount") Integer amount,
                                        @RequestParam(name="promoId", required = false) Integer promoId)
            throws BusinessException {

        //Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");

       /* String token = httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token)){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }*/

       String token = httpServletRequest.getParameterMap().get("token")[0];

       log.info("promoId:"+promoId);

       if(StringUtils.isEmpty(token)){
           throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆不能下单1");
       }

       UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);

        log.info("userModel:"+userModel);
       if(userModel==null){
           throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆不能下单2");
       }

/*       if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }

        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");*/

        log.info("usermodelid:"+userModel.getId());
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId, amount);

        return CommonReturnType.create(orderModel);
    }
}
