package cn.it.ssm.web.controller;

import cn.it.ssm.common.tree.PermNode;
import cn.it.ssm.common.util.PermTreeUtils;
import cn.it.ssm.common.vo.ConResult;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.service.manager.IPermissionService;
import cn.it.ssm.service.manager.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/perm")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRoleService roleService;

    /**
     * 分页查询权限列表
     *
     * @param tableRequest
     * @return
     */
    @GetMapping("/list")
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

    @GetMapping("/{id}")
    @ResponseBody
    public SysPermission findPerm(@PathVariable Integer id) {
        SysPermission permission = permissionService.findPermission(id);
        return permission;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ConResult editPerm(SysPermission permission) {
        permissionService.editPermission(permission);
        return ConResult.success();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ConResult deletePerm(@PathVariable Integer id) {
        boolean b = permissionService.deletePermission(id);
        if (b) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    @GetMapping("/tree")
    @ResponseBody
    public PermNode permTree() {
        List<SysPermission> permissionList = permissionService.findPermissionList();
        return PermTreeUtils.permTree(permissionList);
    }

    @PostMapping("/add")
    @ResponseBody
    public ConResult addPerm(SysPermission permission){
        permission.setId(null);
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        permissionService.addPerm(permission);
        return ConResult.success();
    }

    @GetMapping("/menuList")
    @ResponseBody
    public List<SysPermission> permMenuList() {
        List<SysPermission> permissionList = permissionService.findPermMenuList();
        return permissionList;
    }


}
