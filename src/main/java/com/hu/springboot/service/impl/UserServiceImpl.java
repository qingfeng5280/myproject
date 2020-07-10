package com.hu.springboot.service.impl;

import com.hu.springboot.service.UserService;
import com.hu.springboot.dao.UserDOMapper;
import com.hu.springboot.dao.UserPasswordDOMapper;
import com.hu.springboot.dataobject.UserDO;
import com.hu.springboot.dataobject.UserPasswordDO;
import com.hu.springboot.error.BusinessException;
import com.hu.springboot.error.EmBusinessError;
import com.hu.springboot.service.model.UserModel;
import com.hu.springboot.validator.ValidationResult;
import com.hu.springboot.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserModel getUserById(Integer id) {

        //调用userdomapper获取对应的用户dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (userDO == null) {
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
   /*     if(StringUtils.isEmpty(userModel.getName())||
                userModel.getGender()== null ||
                userModel.getAge()== null ||
                StringUtils.isEmpty(userModel.getTelephone())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR) ;
        }*/


        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg()) ;
        }


        //实现model->dataobject
        UserDO userDO = convertFromModel(userModel);
        userDOMapper.insertSelective(userDO);

        userModel.setId(userDO.getId()); // 获取自增 id

        UserPasswordDO userPasswordDO = convertPassWordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {

        logger.info("进入validateLogin"+telephone);

        UserDO userDO = userDOMapper.selectByTelephone(telephone);


        if (userDO == null) {
            logger.info("抛出异常");

            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);


        //比对用户信息内加密的密码是否和用户的
        if (!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }


    private UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);

        return userDO;
    }

    private UserPasswordDO convertPassWordFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());

        return userPasswordDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);

        if (userPasswordDO != null) {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}
