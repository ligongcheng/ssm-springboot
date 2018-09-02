package cn.it.ssm.service.manager;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.domain.auto.SysPermission;

import java.util.List;

public interface IPermissionService {
    PageListVO findPermissionList(TableRequest tableRequest);


    List<SysPermission> findPermissionList();

    boolean deletePermission(List<SysPermission> permissionList);
}
