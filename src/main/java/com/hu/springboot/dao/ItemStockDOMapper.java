package com.hu.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hu.springboot.dataobject.ItemStockDO;
import org.apache.ibatis.annotations.Param;

//继承mybatis-plus的BaseMapper，但仍然保留了自定义但
public interface ItemStockDOMapper{

    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    ItemStockDO selectByItemId(Integer itemId);

    int decreaseStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount) ;

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);
}