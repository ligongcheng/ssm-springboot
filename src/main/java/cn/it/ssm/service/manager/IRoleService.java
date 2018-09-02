package cn.it.ssm.service.manager;

import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;

import java.util.List;

public interface IRoleService {
    List<SysRole> findAllRoleList();

    boolean addRole(SysRole role);

    boolean editRole(SysRole role);

    boolean deleteRole(List<SysRole> role);

    boolean findRoleExistsUserByRoleId(SysRole sysRole);

    List<SysPermission> findRolePermsByRoleId(SysRole sysRole);

    boolean saveRolePermsByPermIds(Integer roleId, Integer[] rolePermIds);
}
