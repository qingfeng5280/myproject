package com.hu.springboot.service;

import com.hu.springboot.dataobject.ItemDO;
import com.hu.springboot.error.BusinessException;
import com.hu.springboot.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem() ;

    //商品详情浏览
    ItemModel getItemById(Integer id) ;

    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException ;

    //商品销量增加
    void increaseSales(Integer itemId, Integer amount) throws BusinessException ;

    //测试mybatis-plus查询
    ItemDO getItemById2(Integer id);
}
