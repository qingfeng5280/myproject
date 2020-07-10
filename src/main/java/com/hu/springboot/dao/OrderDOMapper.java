package com.hu.springboot.dao;

import com.hu.springboot.dataobject.OrderDO;

public interface OrderDOMapper {
    int deleteByPrimaryKey(byte[] id);

    int insert(OrderDO record);

    int insertSelective(OrderDO record);

    OrderDO selectByPrimaryKey(byte[] id);

    int updateByPrimaryKeySelective(OrderDO record);

    int updateByPrimaryKey(OrderDO record);
}