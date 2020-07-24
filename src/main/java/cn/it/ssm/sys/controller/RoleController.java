package cn.it.ssm.sys.controller;


import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.shiro.util.ShiroConst;
import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.service.RoleService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author chengtao
 * @date 2020/07/18 10:40:23
 */
@Controller
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping("sys/rolePage")
    public String role() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId("2134").list();
        for (Execution execution : executionList) {

        }
        TaskService taskService = processEngine.getTaskService();

        return "sys/role";
    }

    /**
     * 角色列表
     *
     * @return {@link List<SysRole>}
     */
    @GetMapping("sys/role")
    @ResponseBody
    public List<SysRole> roleList() {
        return roleService.findAllRoleList();
    }

    /**
     * 添加角色
     *
     * @param role 角色
     * @return {@link ConResult}
     */
    @PutMapping("sys/role")
    @ResponseBody
    public ConResult addRole(SysRole role) {
        if (role == null) {
            return ConResult.error();
        }
        role.setId(null);
        if (roleService.addRole(role)) {
            return ConResult.success();
        }
        return ConResult.error();
    }


    /**
     * 修改角色
     *
     * @param role        角色
     * @param rolePermIds 角色权限id
     * @return {@link ConResult}
     */
    @PostMapping("sys/role")
    @ResponseBody
    public ConResult editRole(SysRole role, @RequestParam(required = false) Integer[] rolePermIds) {
        roleService.editRole(role);
        roleService.saveRolePermsByPermIds(role.getId(), rolePermIds);
        // 清除该角色下所有的用户权限缓存
        List<SysUser> users = roleService.findUsersByRoleId(role.getId());
        users.forEach(user -> cacheManager.getCache(ShiroConst.AUTHENTIZATION_CACHE).remove(user.getUsername()));
        return ConResult.success();
    }

    /**
     * 删除角色
     *
     * @param role 角色
     * @return {@link ConResult}
     */
    @DeleteMapping("sys/role")
    @ResponseBody
    public ConResult deleteRole(@RequestBody List<SysRole> role) {
        if (role == null || role.size() == 0) {
            return ConResult.error();
        }
        for (SysRole sysRole : role) {
            if (sysRole.getId() == 1) {
                return ConResult.error().addMsg("不能删除系统管理员角色");
            }
            if (roleService.findRoleExistsUserByRoleId(sysRole)) {
                return ConResult.error().addMsg("角色已分配用户，无法删除");
            }
        }
        if (roleService.deleteRole(role)) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    /**
     * 获取角权限
     *
     * @param sysRole 角色
     * @return {@link Integer[]}
     */
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
