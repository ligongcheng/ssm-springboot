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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
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
    @GetMapping("/permList")
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

    @DeleteMapping("/perm")
    @ResponseBody
    public ConResult deletePerm(@RequestBody List<SysPermission> permissionList) {
        boolean b = permissionService.deletePermission(permissionList);
        if (b) {
            return ConResult.success();
        }
        return ConResult.error();
    }

    @GetMapping("/permTree")
    @ResponseBody
    public PermNode permTree() {
        List<SysPermission> permissionList = permissionService.findPermissionList();
        return PermTreeUtils.permTree(permissionList);
    }


}
