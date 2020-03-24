package cn.it.ssm.sys.service.impl;


import cn.it.ssm.sys.domain.auto.*;
import cn.it.ssm.sys.mapper.auto.SysRoleMapper;
import cn.it.ssm.sys.mapper.auto.SysRolePermissionMapper;
import cn.it.ssm.sys.mapper.auto.SysUserRoleMapper;
import com.github.pagehelper.PageHelper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
public class RoleServiceImpl implements cn.it.ssm.sys.service.RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<SysRole> findAllRoleList() {
        return sysRoleMapper.selectByExample(null);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean addRole(SysRole role) {
        return sysRoleMapper.insertSelective(role) != 0;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean editRole(SysRole role) {
        return sysRoleMapper.updateByPrimaryKeySelective(role) != 0;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteRole(List<SysRole> role) {
        SysRoleMapper templateMapper = sqlSessionTemplate.getMapper(SysRoleMapper.class);
        for (SysRole sysRole : role) {
            //删除角色权限
            SysRolePermissionExample example = new SysRolePermissionExample();
            example.createCriteria().andSysRoleIdEqualTo(sysRole.getId());
            sysRolePermissionMapper.deleteByExample(example);
            //删除角色
            templateMapper.deleteByPrimaryKey(sysRole.getId());
        }


        return true;
    }

    @Override
    public boolean findRoleExistsUserByRoleId(SysRole sysRole) {
        SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = sysUserRoleExample.createCriteria();
        criteria.andSysRoleIdEqualTo(sysRole.getId());
        PageHelper.startPage(1, 1);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByExample(sysUserRoleExample);
        return sysUserRoles.size() != 0;
    }

    @Override
    public List<SysPermission> findRolePermsByRoleId(SysRole sysRole) {
        return sysRoleMapper.findRolePermsByRoleId(sysRole);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean saveRolePermsByPermIds(Integer roleId, Integer[] rolePermIds) {
        sysRoleMapper.deleteRolePermsByRoleId(roleId);
        for (Integer rolePermId : rolePermIds) {
            sysRoleMapper.saveRolePermsByPermId(roleId, rolePermId);
        }
        return true;
    }
}
