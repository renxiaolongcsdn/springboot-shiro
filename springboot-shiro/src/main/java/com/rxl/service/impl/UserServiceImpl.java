package com.rxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxl.dao.*;
import com.rxl.entity.*;
import com.rxl.service.UserService;
import com.rxl.utils.SaltUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    RolePermsDao rolePermsDao;

    @Autowired
    PermsDao permsDao;

    @Autowired
    RoleDao roleDao;

    @Override
    public void register(UserEntity userEntity) {
        String salt = SaltUtils.getSalt(8);
        userEntity.setSalt(salt);
        //存储加密后的密码
        Md5Hash md5Hash = new Md5Hash(userEntity.getPassword(), salt, 1024);
        userEntity.setPassword(md5Hash.toHex());
        this.save(userEntity);
    }

    @Override
    public UserEntity queryByName(String userName) {
        LambdaQueryWrapper<UserEntity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername,userName);
        return ObjectUtils.isEmpty(getOne(queryWrapper)) ? null : getOne(queryWrapper) ;
    }

    @Override
    public UserEntity findRolesByUserName(String username) {
        UserEntity userEntity = queryByName(username);
        LambdaQueryWrapper<UserRoleEntity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleEntity::getUserid,userEntity.getId());
        List<UserRoleEntity> userRoleEntities = userRoleDao.selectList(queryWrapper);
        List<RoleEntity> roles=new ArrayList<>();
        userRoleEntities.forEach(ite->{
            roles.add(roleDao.selectById(ite.getRoleid()));
        });
        userEntity.setRoles(roles);
        return userEntity;
    }


    @Override
    public List<PermsEntity> findPermsByRoleId(String id) {
        LambdaQueryWrapper<RolePermsEntity> query=new LambdaQueryWrapper<>();
        query.eq(RolePermsEntity::getRoleid,id);
        List<RolePermsEntity> rolePermsEntities = rolePermsDao.selectList(query);
        CopyOnWriteArrayList<Integer> permsId=new CopyOnWriteArrayList<>();
        rolePermsEntities.forEach(ite->{
            permsId.add(ite.getPermsid());
        });
        LambdaQueryWrapper<PermsEntity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(PermsEntity::getId,permsId);
        List<PermsEntity> permsEntities = permsDao.selectList(queryWrapper);
        return permsEntities;
    }

    @Override
    public UserEntity login(String userName, String password) {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(userName,password));
        }catch (UnknownAccountException e){
            e.printStackTrace();
            throw new RuntimeException("用户名错误!");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            throw new RuntimeException("密码错误!");
        }
        String username = (String) subject.getPrincipal();
        UserEntity userEntity = queryByName(userName);
        //认证通过

        //判断授权是否有admin 角色  和 user:update:01 操作权限
        if(subject.isAuthenticated()){
            //基于角色权限管理
            boolean admin = subject.hasRole("admin");
            System.out.println(admin);
/*            boolean permitted = subject.isPermitted("user:update:01");
            System.out.println(permitted);*/
        }
        return userEntity;
    }
}
