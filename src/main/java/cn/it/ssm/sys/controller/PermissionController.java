package cn.it.ssm.sys.controller;

import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.util.PermTreeUtils;
import cn.it.ssm.common.util.tree.PermNode;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.service.PermissionService;
import cn.it.ssm.sys.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @GetMapping("sys/permPage")
    public String perm() {
        return "sys/perm";
    }

    /**
     * 分页查询权限列表
     *
     * @param tableRequest
     * @return
     */
    @GetMapping("sys/perm")
    @ResponseBody
    public PageListVO getPermList(TableRequest tableRequest) {
        if (tableRequest == null) {
            tableRequest = new TableRequest(5, 1, null);
        } else {
            if (tableRequest.getPageNum() == null) tableRequest.setPageNum(1);
            if (tableRequest.getPageSize() == null) tableRequest.setPageSize(5);
        }
        PageListVO pageListVO = permissionService.findPermissionList(tableRequest);
        return pageListVO;
    }

    @GetMapping("sys/perm/{id}")
    @ResponseBody
    public SysPermission findPerm(@PathVariable Integer id) {
        SysPermission permission = permissionService.findPermission(id);
        return permission;
    }

    @PutMapping("sys/perm/{id}")
    @ResponseBody
    public ConResult editPerm(SysPermission permission) {
        permissionService.editPermission(permission);
        return ConResult.success();
    }

    @DeleteMapping("sys/perm/{id}")
    @ResponseBody
    public ConResult deletePerm(@PathVariable Integer id) {
        boolean b = permissionService.deletePermission(id);
        if (b) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    @GetMapping("sys/perm/tree")
    @ResponseBody
    public PermNode permTree() {
        List<SysPermission> permissionList = permissionService.findPermissionList();
        PermNode permTree = PermTreeUtils.permTree(permissionList);
        return permTree;
    }

    @PostMapping("sys/perm")
    @ResponseBody
    public ConResult addPerm(SysPermission permission) {
        permission.setId(null);
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        permissionService.addPerm(permission);
        //添加权限时将权限分配给系统管理员
        List<SysPermission> permissionList = permissionService.findPermissionList();
        Integer[] permIds = new Integer[permissionList.size()];
        for (int i = 0; i < permissionList.size(); i++) {
            permIds[i] = permissionList.get(i).getId();
        }
        roleService.saveRolePermsByPermIds(1, permIds);
        return ConResult.success();
    }

    @GetMapping("sys/perm/menuList")
    @ResponseBody
    public List<SysPermission> permMenuList() {
        List<SysPermission> permissionList = permissionService.findPermMenuList();
        return permissionList;
    }


}
