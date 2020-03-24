package cn.it.ssm.sys.service;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.sys.domain.auto.SysPermission;

import java.util.List;

public interface PermissionService {
    PageListVO findPermissionList(TableRequest tableRequest);


    List<SysPermission> findPermissionList();

    boolean deletePermission(Integer id);

    List<SysPermission> findPermMenuList();

    boolean addPerm(SysPermission permission);

    SysPermission findPermission(Integer id);

    Boolean editPermission(SysPermission permission);
}
