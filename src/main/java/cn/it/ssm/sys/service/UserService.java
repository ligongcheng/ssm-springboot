package cn.it.ssm.sys.service;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.domain.vo.SysUserWithRole;

import java.util.List;
import java.util.Map;

public interface UserService {

    SysUser findByUserId(String id);

    List<SysUser> getUserPageList();

    boolean addResgiter(SysUser user);


    List<SysUser> findByUserName(String username);

    boolean checkExistByUserName(String username);

    List<SysRole> findRoles(String username);

    List<SysPermission> findPermissions(String username);

    boolean deleteUserList(List<SysUser> userList);

    boolean editUserInfo(SysUserWithRole user);

    List<SysUserWithRole> findUserWithRole(SysUser sysUser);

    PageListVO findUserWithRoleList(TableRequest tableRequest);

    boolean updateUserLoginInfo(SysUser user);

    int deleteUser(String id);

    int disableUser(String id);

    int enableUser(String id);

    List<SysUser> findUserByRoleName(String roleName);

    Map<String, String> findUserFilterMap();
}
