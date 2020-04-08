package cn.it.ssm.sys.dao;


import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.domain.vo.SysUserWithRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapperFix {

    List<SysRole> findRolesByUsername(@Param("username") String username);

    List<SysRole> findRolesByUserId(String id);

    List<SysPermission> findPermissionsByRoleId(@Param("roleId") Integer[] id);

    List<SysUserWithRole> findUserWithRole(SysUser sysUser);

    List<SysUserWithRole> findUserListWithRoles(@Param("search") String search);

    List<SysUser> findUserByRoleName(@Param("roleName") String roleName);
}
