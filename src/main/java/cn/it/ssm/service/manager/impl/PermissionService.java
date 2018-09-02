package cn.it.ssm.service.manager.impl;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysPermissionExample;
import cn.it.ssm.mapper.auto.SysPermissionMapper;
import cn.it.ssm.service.manager.IPermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
public class PermissionService implements IPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public PageListVO findPermissionList(TableRequest tableRequest) {

        SysPermissionExample example = new SysPermissionExample();
        SysPermissionExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(tableRequest.getSearch())) {
            criteria.andNameLike("%" + tableRequest.getSearch() + "%");
        }
        //criteria.andAvailableEqualTo(1);
        PageHelper.startPage(tableRequest.getPageNum(), tableRequest.getPageSize());
        List<SysPermission> permissionList = sysPermissionMapper.selectByExample(example);
        PageInfo<SysPermission> pageInfo = new PageInfo<>(permissionList);
        PageListVO pageListVO = new PageListVO();
        pageListVO.setTotal(pageInfo.getTotal());
        pageListVO.setRows(pageInfo.getList());
        return pageListVO;
    }

    @Override
    public List<SysPermission> findPermissionList() {
        List<SysPermission> permissionList = sysPermissionMapper.selectByExample(null);
        return permissionList;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deletePermission(List<SysPermission> permissionList) {

        ArrayList<Integer> list = new ArrayList<>();
        for (SysPermission p : permissionList) {
            list.add(p.getId());
        }

        return sysPermissionMapper.deletePermission(list) != 0;
    }
}
