package com.roli.apsimock.dao.user;

import com.roli.apsimock.model.user.UserFieldForget;
import com.roli.apsimock.model.user.UserInfo;
import com.roli.apsimock.model.user.userov.UserInfoOV;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxinyu
 * @date 2018/3/2 下午4:54
 */
public interface UserInfoMapper {
    //新增用户
    public void insertUserInfo(UserInfoOV userInfoOv);

    //查询当前项目下的用户
    public List<UserInfo> queryAllUser(@Param("projectid") Integer projectid,
                                       @Param("isInv") Integer isInv,
                                       @Param("userAccount")String userAccount,
                                       @Param("userName")String userName);
    
    //按照账户名查询用户
    public UserInfo queryUserInfoByAccount(@Param("userAccount") String account);

    public Integer queryUserIdByAccount(@Param("userAccount") String account);

    //按照账户名和用户姓名查询
    public UserInfo queryUserInfoByAccountAndName(UserFieldForget userFieldForget);
    //按照账号名进行密码修改
    public void updateAccountPass(@Param("userAccount") String account,@Param("newPass")String newPass);
    //根据用户id查询用户名称
    public String queryUserNameByUserid(@Param("userId") Integer userId);
    //根据用户id更改用户账号
    public void updateAccountById(@Param("newUserAccount") String account,@Param("userId")String userId);
    //根据用户id更改用户名
    public void updateUserNameById(@Param("newUserName") String userName,@Param("userId")String userId);
    //根据用户id和用户名查询是否重名
    public int queryAccountCountById(@Param("newUserAccount") String newUserAccount);

}
