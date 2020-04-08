package cn.it.ssm.common.shiro.realm;


import cn.it.ssm.common.shiro.util.MySimpleByteSource;
import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.List;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<SysRole> roleList = userService.findRoles(sysUser.getUsername());
        HashSet<String> roles = new HashSet<>();
        for (SysRole role : roleList) {
            roles.add(role.getName());
        }
        //设置用户拥有的角色
        authorizationInfo.setRoles(roles);
        List<SysPermission> permissionList = userService.findPermissions(sysUser.getUsername());
        HashSet<String> ps = new HashSet<>();
        for (SysPermission sysPermission : permissionList) {
            ps.add(sysPermission.getPercode());
        }
        //设置用户拥有的权限
        authorizationInfo.setStringPermissions(ps);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String) token.getPrincipal();

        SysUser user = new SysUser();
        user.setUsername(username);

        List<SysUser> userList = userService.findByUserName(username);

        if (userList == null || userList.size() == 0) {
            throw new UnknownAccountException();//没找到帐号
        }

        user = userList.get(0);
        if (Boolean.TRUE.equals(user.getIsDelete() == 1)) {
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        String password = user.getPassword();
        user.setPassword(null);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
            user, //用户
            password, //密码
            new MySimpleByteSource(user.getAuthSalt()),//salt=username+salt
            getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        //调用父类方法，清除缓存
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        try {
            getAuthorizationCache().clear();
        } catch (Exception e) {
        }
    }

    public void clearAllCachedAuthenticationInfo() {
        try {
            getAuthenticationCache().clear();
        } catch (Exception e) {
        }
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
