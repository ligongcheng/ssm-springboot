package cn.it.ssm.sys.service;

import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysUser;

import java.util.List;

public interface RoleService {
    List<SysRole> findAllRoleList();

    boolean addRole(SysRole role);

    boolean editRole(SysRole role);

    boolean deleteRole(List<SysRole> role);

    boolean findRoleExistsUserByRoleId(SysRole sysRole);

    List<SysPermission> findRolePermsByRoleId(SysRole sysRole);

    boolean saveRolePermsByPermIds(Integer roleId, Integer[] rolePermIds);

    List<SysUser> findUsersByRoleId(Integer roleId);
}
