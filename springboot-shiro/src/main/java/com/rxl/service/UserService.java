package com.rxl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rxl.entity.PermsEntity;
import com.rxl.entity.UserEntity;

import java.util.List;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description
 */
public interface UserService extends IService<UserEntity> {

    void register(UserEntity userEntity);

    UserEntity queryByName(String userName);

    //根据用户名查询所有角色
    UserEntity findRolesByUserName(String username);

    //根据角色id查询权限集合
    List<PermsEntity> findPermsByRoleId(String id);

    UserEntity login(String userName,String password);

}
