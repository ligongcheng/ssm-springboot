package cn.it.ssm.mapper;


import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;
import cn.it.ssm.domain.auto.SysUser;
import cn.it.ssm.domain.vo.SysUserWithRole;

import java.util.List;

public interface SysUserMapperFix {

    List<SysRole> findRolesByUsername(String username);

    List<SysRole> findRolesByPrimaryKey(String id);

    List<SysPermission> findPermissionsByRoleId(Integer[] id);

    List<SysUserWithRole> findUserWithRole(SysUser sysUser);


}