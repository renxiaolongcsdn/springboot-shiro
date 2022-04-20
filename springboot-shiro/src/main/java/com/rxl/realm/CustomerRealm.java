package com.rxl.realm;

import com.rxl.entity.PermsEntity;
import com.rxl.entity.UserEntity;
import com.rxl.service.UserService;
import com.rxl.utils.MyByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description 自定义shiro realm
 */
public class CustomerRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("=============进入到shiro 授权 ===========");
        //获取身份信息
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //获取当前角色
        UserEntity rolesByUserName = userService.findRolesByUserName(userName);
        if (!ObjectUtils.isEmpty(rolesByUserName.getRoles())) {  //如果当前用户有角色则进行查询用户权限
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            rolesByUserName.getRoles().forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());  //设置角色名称
                System.out.println("用户["+userName+"]角色=>"+role.getName());
                //查询权限信息
                List<PermsEntity> perms = userService.findPermsByRoleId(role.getId());
                if (!ObjectUtils.isEmpty(perms)) {
                    perms.forEach(per -> {
                        System.out.println("权限=>"+per.getName());
                        simpleAuthorizationInfo.addStringPermission(per.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }


    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("=============进入到shiro 认证 ===========");
        //获取shiro 主体 里面含有身份信息(用户名)
        String Name = (String) authenticationToken.getPrincipal();
        UserEntity userEntity = userService.queryByName(Name);
        if (!ObjectUtils.isEmpty(userEntity)) {
            //参数1 : 用户名
            //参数2 :用户名密码
            //参数3 :盐
            //参数4 : 当前realm 的名称
            System.out.println("认证通过，该用户为："+Name);
            return new SimpleAuthenticationInfo(userEntity.getUsername(), userEntity.getPassword(), new MyByteSource(userEntity.getSalt()), this.getName());
        }
        return null;
    }
}
