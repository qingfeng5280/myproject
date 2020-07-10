package com.hu.springboot.service.impl;

import com.hu.springboot.dao.OrderDOMapper;
import com.hu.springboot.dao.SequenceDOMapper;
import com.hu.springboot.dataobject.OrderDO;
import com.hu.springboot.dataobject.SequenceDO;
import com.hu.springboot.error.BusinessException;
import com.hu.springboot.error.EmBusinessError;
import com.hu.springboot.service.ItemService;
import com.hu.springboot.service.OrderService;
import com.hu.springboot.service.UserService;
import com.hu.springboot.service.model.ItemModel;
import com.hu.springboot.service.model.OrderModel;
import com.hu.springboot.service.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService ;

    @Autowired
    private UserService userService ;

    @Autowired
    private OrderDOMapper orderDOMapper ;

    @Autowired
    private SequenceDOMapper sequenceDOMapper ;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {

            //1、校验下单状态、下单的商品是否存在，用户是否合法、购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId) ;
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在") ;
        }

        UserModel userModel = userService.getUserById(userId) ;
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在") ;
        }

        if(amount <= 0 || amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确") ;
        }

        // 校验活动信息
/*        if(promoId != null){
            if(promoId.intValue() != itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            }

            if(itemModel.getPromoModel().getStatus().intValue() !=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            }

        }*/


        //2、落单减库存
        boolean result = itemService.decreaseStock(itemId,amount) ;
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH) ;
        }

        //3、订单入库
        OrderModel orderModel = new OrderModel() ;
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);

        if(promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }


        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        orderModel.setId(generateOrderNo());

        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        itemService.increaseSales(itemId, amount);

        return orderModel;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)//不与创建订单在一个事务
    public String generateOrderNo(){

        StringBuilder stringBuilder = new StringBuilder();
        // 前 8 位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        //中间 6 位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();

        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceStr = String.valueOf(sequence);
        for(int i=0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        // 最后 2 位为分库分表位
        stringBuilder.append("00");

        return stringBuilder.toString();
    }


    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }

        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());

        return orderDO;
    }
}
