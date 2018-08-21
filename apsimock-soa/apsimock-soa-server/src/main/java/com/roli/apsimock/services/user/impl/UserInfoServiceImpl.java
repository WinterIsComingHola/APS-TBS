package com.roli.apsimock.services.user.impl;

import com.roli.apsimock.dao.user.UserInfoMapper;
import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import com.roli.apsimock.services.user.UserInfoService;
import com.roli.common.exception.BusinessException;
import com.roli.common.exception.CustomAssert;
import com.roli.common.exception.SecurityException;
import com.roli.common.model.enums.ErrorsEnum;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.roli.common.utils.security.AESHandle.*;

/**
 * @author xuxinyu
 * @date 2018/3/5 下午4:56
 */
@Service
public class UserInfoServiceImpl implements UserInfoService{

    @Resource
    UserInfoMapper userInfoMapper;

    /**
    * 新增一个用户
    * @param userInfoOV
    * @return
    * @throws SecurityException
    * @author xuxinyu
    * @date 2018/3/6 下午7:19
    */
    @Override
    @Transactional
    public void addUser(UserInfoOV userInfoOV) throws SecurityException, BusinessException {
        //校验UserInfoOV对象
        CustomAssert.isNotNull(userInfoOV, ErrorsEnum.OBJECT_NULL);
        CustomAssert.isNotEmpty(userInfoOV.getUserAccount(),ErrorsEnum.ACCOUNT_NULL);
        CustomAssert.isNotEmpty(userInfoOV.getUserName(),ErrorsEnum.USERNAME_NULL);

        //校验密码一致性
        String pass1 = userInfoOV.getPassWord();
        String pass2 = userInfoOV.getPassConfirm();
        if(!pass1.equals(pass2)){
            BusinessException.throwMessage(ErrorsEnum.PASS_NOT_SAME);
        }

        //校验数据库是否已经存在用户数据
        UserInfo userInfo = userInfoMapper.queryUserInfoByAccount(userInfoOV.getUserAccount());
        if(userInfo!=null){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DUPLICATE);
        }

        //密码加密
        userInfoOV.setPassWord(encryptAES(null,pass1));
        //生成时间
        LocalDateTime localDateTime = LocalDateTime.now();
        userInfoOV.setCreateTime(localDateTime);

        userInfoMapper.insertUserInfo(userInfoOV);

    }

    /**
    * 查询一个用户信息
    * @param
    * @return ${return_type}
    * @throws
    * @author xuxinyu
    * @date 2018/3/13 下午7:42
    */
    @Override
    public UserInfo queryUser(UserInfo userInfo) throws SecurityException, BusinessException {

        //校验UserInfoOV对象
        CustomAssert.isNotNull(userInfo, ErrorsEnum.OBJECT_NULL);
        CustomAssert.isNotEmpty(userInfo.getUserAccount(),ErrorsEnum.ACCOUNT_NULL);

        UserInfo user = userInfoMapper.queryUserInfoByAccount(userInfo.getUserAccount());

        if(user==null){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_NONE);
        }
        if(!(encryptAES(null,userInfo.getPassWord())).equals(user.getPassWord())){
            BusinessException.throwMessage(ErrorsEnum.PASS_CHECK_FAIL);
        }

        return user;
    }

    @Override
    public Integer queryUserIdByAccount(String account) throws BusinessException {
        CustomAssert.isNotEmpty(account,ErrorsEnum.ACCOUNT_NULL);
        Integer userid = userInfoMapper.queryUserIdByAccount(account);
        return userid;
    }

    @Override
    public boolean queryUserInfoByAccountAndName(UserFieldForget userFieldForget) throws BusinessException {

        CustomAssert.isNotNull(userFieldForget, ErrorsEnum.OBJECT_NULL);
        UserInfo user = userInfoMapper.queryUserInfoByAccountAndName(userFieldForget);

        if(user==null){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DATA_NULL);
        }
        return true;

    }

    @Override
    @Transactional
    public void updateUserPass(Map<String,String> userAccountAndNewpass) throws BusinessException, SecurityException {
        CustomAssert.isNotNull(userAccountAndNewpass, ErrorsEnum.OBJECT_NULL);
        String userAccount = userAccountAndNewpass.get("userAccount");
        String newPass = userAccountAndNewpass.get("newPass");
        String newPassEn = encryptAES(null,newPass);

        Integer userid = userInfoMapper.queryUserIdByAccount(userAccount);
        if(userid==null){
            BusinessException.throwMessage(ErrorsEnum.ACCOUNT_DATA_NULL);
        }

        userInfoMapper.updateAccountPass(userAccount,newPassEn);

    }

    @Override
    @Transactional
    public List<UserInfo> queryAllUser(String projectid,
                                       Integer isInv,
                                       String userAccount,
                                       String userName) throws BusinessException {
        CustomAssert.isNotEmpty(projectid,ErrorsEnum.PARAM_NULL);
        CustomAssert.isNotNull(isInv,ErrorsEnum.PARAM_NULL);

        Integer proid = Integer.parseInt(projectid);
        List<UserInfo> userInfoList = userInfoMapper.queryAllUser(proid,isInv,userAccount,userName);
        return userInfoList;
    }

}
