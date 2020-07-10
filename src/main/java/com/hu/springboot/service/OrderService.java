package com.hu.springboot.service;

import com.hu.springboot.error.BusinessException;
import com.hu.springboot.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;

}
