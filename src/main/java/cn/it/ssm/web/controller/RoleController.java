package cn.it.ssm.web.controller;


import cn.it.ssm.common.shiro.realm.UserRealm;
import cn.it.ssm.common.vo.ConResult;
import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;
import cn.it.ssm.service.manager.IRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("sys/rolePage")
    public String role() {
        return "sys/role";
    }

    @GetMapping("sys/role")
    @ResponseBody
    public List<SysRole> roleList() {
        return roleService.findAllRoleList();
    }

    @PutMapping("sys/role")
    @ResponseBody
    public ConResult addRole(SysRole role) {
        if (role == null) return ConResult.error();
        role.setId(null);
        if (roleService.addRole(role)) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    @PostMapping("sys/role")
    @ResponseBody
    public ConResult editRole(SysRole role, @RequestParam(required = false) Integer[] rolePermIds) {
        roleService.editRole(role);
        roleService.saveRolePermsByPermIds(role.getId(), rolePermIds);
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearAllCachedAuthorizationInfo();
        return ConResult.success();
    }

    @DeleteMapping("sys/role")
    @ResponseBody
    public ConResult deleteRole(@RequestBody List<SysRole> role) {
        if (role == null || role.size() == 0) return ConResult.error();
        for (SysRole sysRole : role) {
            if (roleService.findRoleExistsUserByRoleId(sysRole)) {
                return ConResult.error().addMsg("角色已分配用户，无法删除");
            }
        }
        if (roleService.deleteRole(role)) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    @GetMapping("sys/rolePermIds")
    @ResponseBody
    public Integer[] getRolePerms(SysRole sysRole) {
        List<SysPermission> rolePermsList = roleService.findRolePermsByRoleId(sysRole);
        Integer[] arr = new Integer[rolePermsList.size()];
        for (int i = 0; i < rolePermsList.size(); i++) {
            SysPermission sysPermission = rolePermsList.get(i);
            arr[i] = sysPermission.getId();
        }
        return arr;
    }
}
