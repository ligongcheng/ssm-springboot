package cn.it.ssm.mapper;


import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;
import cn.it.ssm.domain.auto.SysUser;
import cn.it.ssm.domain.vo.SysUserWithRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapperFix {

    List<SysRole> findRolesByUsername(@Param("username") String username);

    List<SysRole> findRolesByUserId(String id);

    List<SysPermission> findPermissionsByRoleId(@Param("roleId") Integer[] id);

    List<SysUserWithRole> findUserWithRole(SysUser sysUser);

    List<SysUserWithRole> findUserListWithRoles(@Param("search") String search);


}