package cn.it.ssm.service.manager.impl;


import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.domain.auto.*;
import cn.it.ssm.domain.vo.SysUserWithRole;
import cn.it.ssm.mapper.SysUserMapperFix;
import cn.it.ssm.mapper.auto.SysUserMapper;
import cn.it.ssm.mapper.auto.SysUserRoleMapper;
import cn.it.ssm.service.manager.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
public class UserService implements IUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private SysUserMapperFix sysUserMapperFix;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUser findByUserId(String id) {
        return sysUserMapper.selectByPrimaryKey(id);

    }

    @Override
    public List<SysUser> getUserPageList() {
        return sysUserMapper.selectByExample(null);

    }

    @Override
    @Transactional(readOnly = false)
    public boolean addResgiter(SysUser user) {
        sysUserMapper.insertSelective(user);
        SysUserRole sysUserRole = new SysUserRole();
        return true;
    }

    @Override
    public List<SysUser> findByUserName(String username) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria userExampleCriteria = sysUserExample.createCriteria();
        userExampleCriteria.andUsernameEqualTo(username);
        return sysUserMapper.selectByExample(sysUserExample);
    }

    @Override
    public boolean checkExistByUserName(String username) {
        List<SysUser> userList = findByUserName(username);
        return userList != null && userList.size() != 0;
    }

    @Override
    public List<SysRole> findRoles(String username) {
        return sysUserMapperFix.findRolesByUsername(username);
    }


    @Override
    public List<SysPermission> findPermissions(String username) {
        List<SysRole> roleList = findRoles(username);
        Integer[] roleId = new Integer[roleList.size()];
        for (int i = 0; i < roleList.size(); i++) {
            roleId[i] = roleList.get(i).getId();
        }

        return sysUserMapperFix.findPermissionsByRoleId(roleId);
    }


    @Override
    @Transactional(readOnly = false)
    public boolean deleteUserList(List<SysUser> userList) {
        SysUserMapper templateMapper = sqlSessionTemplate.getMapper(SysUserMapper.class);
        SysUser sysUser = new SysUser();
        for (SysUser user : userList) {
            sysUser.setId(user.getId());
            sysUser.setIsDelete(1);
            templateMapper.updateByPrimaryKeySelective(sysUser);
        }

        return true;
    }


    @Override
    @Transactional(readOnly = false)
    public boolean editUserInfo(SysUserWithRole user) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(user.getUsername());
        sysUser.setAge(user.getAge());
        sysUser.setNickname(user.getNickname());
        sysUser.setId(user.getId());
        sysUser.setIsDelete(user.getIsDelete());
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setSysRoleId(user.getRoleId());
        sysUserRole.setSysUserId(user.getId());
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (user.getRoleId() != null) {
            //更新角色信息
            SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
            SysUserRoleExample.Criteria criteria = sysUserRoleExample.createCriteria();
            criteria.andSysUserIdEqualTo(user.getId());
            List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByExample(sysUserRoleExample);
            //List<SysUserWithRole> userWithRole = sysUserMapperFix.findUserWithRole(sysUser);
            if (sysUserRoles.size() == 0) {
                //该用户还没有角色信息，插入角色信息
                sysUserRoleMapper.insertSelective(sysUserRole);
            } else {
                //更新角色信息
                sysUserRoleMapper.updateByExampleSelective(sysUserRole, sysUserRoleExample);
            }

        }
        return count != 0;
    }

    @Override
    public List<SysUserWithRole> findUserWithRole(SysUser sysUser) {
        List<SysUserWithRole> userWithRole = sysUserMapperFix.findUserWithRole(sysUser);
        return userWithRole;
    }

    @Override
    public PageListVO findUserWithRoleList(TableRequest tableRequest) {
        if (tableRequest == null) {
            tableRequest = new TableRequest(5, 1, null);
        } else {
            if (tableRequest.getPageNum() == null) tableRequest.setPageNum(1);
            if (tableRequest.getPageSize() == null) tableRequest.setPageSize(5);
        }
        PageHelper.startPage(tableRequest.getPageNum(), tableRequest.getPageSize());
        List<SysUserWithRole> userWithRoleList = sysUserMapperFix.findUserWithRole(null);
        PageInfo<SysUserWithRole> pageInfo = new PageInfo<>(userWithRoleList);
        PageListVO pageListVO = new PageListVO();
        pageListVO.setRows(pageInfo.getList());
        pageListVO.setTotal(pageInfo.getTotal());
        return pageListVO;
    }

    @Override
    @Transactional(readOnly = false)
    public boolean updateUserLoginInfo(SysUser user) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        sysUserMapper.updateByExampleSelective(user, sysUserExample);
        return true;
    }


}
