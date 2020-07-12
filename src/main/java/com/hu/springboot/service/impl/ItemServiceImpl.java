package com.hu.springboot.service.impl;

import com.hu.springboot.dao.ItemDOMapper;
import com.hu.springboot.dao.ItemStockDOMapper;
import com.hu.springboot.dataobject.ItemDO;
import com.hu.springboot.dataobject.ItemStockDO;
import com.hu.springboot.error.BusinessException;
import com.hu.springboot.error.EmBusinessError;
import com.hu.springboot.service.ItemService;
import com.hu.springboot.service.model.ItemModel;
import com.hu.springboot.validator.ValidationResult;
import com.hu.springboot.validator.ValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator ;

    @Autowired
    private ItemDOMapper itemDOMapper ;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper ;


    private Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private ItemDO convertItemDOFromItemModel(ItemModel itemmodel){
        if(itemmodel == null){
            return null ;
        }
        ItemDO itemDO = new ItemDO() ;
        BeanUtils.copyProperties(itemmodel,itemDO);
        itemDO.setPrice(itemmodel.getPrice().doubleValue());
        return itemDO ;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemmodel){
        if(itemmodel == null){
            return null ;
        }
        ItemStockDO itemStockDO = new ItemStockDO() ;
        itemStockDO.setItemId(itemmodel.getId());
        itemStockDO.setStock(itemmodel.getStock());
        return itemStockDO ;
    }




    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        logger.info("进入createItem");

        //校验入参
        ValidationResult result = validator.validate(itemModel) ;


        logger.info("校验结束"+result.isHasErrors()+result.getErrMsg());


        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg()) ;
        }

        //转化itemmodel-dataocject
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel) ;

        //写入数据库
        itemDOMapper.insertSelective(itemDO) ;
        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel) ;

        itemStockDOMapper.insertSelective(itemStockDO) ;

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem() ;
        List<ItemModel> itemModelList =  itemDOList.stream().map(itemDO ->{
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO,itemStockDO) ;
            return itemModel ;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id) ;
        if(itemDO == null){
            return null ;
        }
        //操作获得库存的数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id) ;

        //将dataobject->model
        ItemModel itemModel = convertModelFromDataObject(itemDO,itemStockDO) ;

        //获取活动商品信息
      /*  PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel != null && promoModel.getStatus().intValue() != 3){
            itemModel.setPromoModel(promoModel);
        }*/

        return itemModel;
    }

    @Override
    public ItemDO getItemById2(Integer id) {
        logger.info("id为："+id);
        ItemDO item = itemDOMapper.selectById(id) ;
        System.out.println(item);
        return item;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
       //此处直接判断受影响的记录条数，而不用先去数据库查询库存是否够，减少一次数据库操作，提升性能
        int affectedRow = itemStockDOMapper.decreaseStock(itemId,amount) ;
       if(affectedRow > 0){
           //更新库存成功
           return true ;
       }else{
           //更新库存失败
           return false ;
       }

    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId,amount) ;
     }

    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel() ;
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());

        return itemModel ;
    }
}
