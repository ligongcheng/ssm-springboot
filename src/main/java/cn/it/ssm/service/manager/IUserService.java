package cn.it.ssm.service.manager;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;
import cn.it.ssm.domain.auto.SysUser;
import cn.it.ssm.domain.vo.SysUserWithRole;

import java.util.List;

public interface IUserService {

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
}
