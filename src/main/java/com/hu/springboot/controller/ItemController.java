package com.hu.springboot.controller;

import com.hu.springboot.controller.viewobject.ItemVO;
import com.hu.springboot.dataobject.ItemDO;
import com.hu.springboot.error.BusinessException;
import com.hu.springboot.response.CommonReturnType;
import com.hu.springboot.service.CacheService;
import com.hu.springboot.service.ItemService;
import com.hu.springboot.service.model.ItemModel;
import lombok.extern.log4j.Log4j;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/item")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
@Log4j
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CacheService cacheService;


    //创建商品的controller
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        log.info("进入create**********************");

        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);
    }


    //商品详情页浏览   多级缓存，本地热点缓存存在脏读
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){

        ItemModel itemModel = null;

        //先取本地缓存
        itemModel = (ItemModel) cacheService.getFromCommonCache("item_"+id);

        if(itemModel==null){
            //根据商品id到redis内获取
            itemModel = (ItemModel) redisTemplate.opsForValue().get("item_"+id);

            //若redis内不存在对应到itemmodel则访问下游到service
            if(itemModel==null){
                itemModel = itemService.getItemById(id) ;
                //设置itemModel到redis内
                redisTemplate.opsForValue().set("item_"+id,itemModel);
                redisTemplate.expire("item_"+id,10, TimeUnit.MINUTES);
            }
            //填充本地缓存
            cacheService.setCommonCache("item_"+id,itemModel);
        }

        ItemVO itemVO = convertVOFromModel(itemModel) ;
        return CommonReturnType.create(itemVO) ;
    }


    //商品列表页面浏览
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){

        log.info("进入list");

        List<ItemModel> itemModelList = itemService.listItem();

        //使用stream api将list内的itemModel转化为itemVO
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel ->{
            ItemVO itemVO = this.convertVOFromModel(itemModel) ;
            return itemVO ;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList) ;
    }


    private ItemVO convertVOFromModel(ItemModel itemModel) {

        if (itemModel == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if(itemModel.getPromoModel() != null){
            //有正在进行或即将进行的秒杀
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }


    @RequestMapping(value = "/get1", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem1(@RequestParam(name = "id")Integer id){

        log.info("进入get1:");
        ItemDO Item = null;

        Item = itemService.getItemById2(id) ;

        return CommonReturnType.create(Item) ;
    }

}
