package cn.it.ssm.sys.service.impl;


import cn.it.ssm.common.ExceptionHandler.AccountChangeException;
import cn.it.ssm.common.shiro.util.ShiroConst;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.sys.dao.SysUserMapperFix;
import cn.it.ssm.sys.dao.auto.SysUserMapper;
import cn.it.ssm.sys.dao.auto.SysUserRoleMapper;
import cn.it.ssm.sys.domain.auto.*;
import cn.it.ssm.sys.domain.vo.SysUserWithRole;
import cn.it.ssm.sys.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.cache.CacheManager;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private SysUserMapperFix sysUserMapperFix;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private CacheManager cacheManager;

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
        BeanUtils.copyProperties(user, sysUser);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setSysRoleId(user.getRoleId());
        sysUserRole.setSysUserId(user.getId());
        int count = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        boolean modify = false;
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
                modify = true;
            } else {
                //角色id改变则更新角色信息
                if (sysUserRoles.get(0).getSysRoleId().equals(user.getRoleId())) {
                    sysUserRoleMapper.updateByExampleSelective(sysUserRole, sysUserRoleExample);
                    modify = true;
                }
            }
        }
        // 角色信息已修改，清除权限缓存
        if (modify) {
            cacheManager.getCache(ShiroConst.AUTHENTIZATION_CACHE).remove(user.getUsername());
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
            if (tableRequest.getPageNum() == null) {
                tableRequest.setPageNum(1);
            }
            if (tableRequest.getPageSize() == null) {
                tableRequest.setPageSize(5);
            }
        }
        PageHelper.startPage(tableRequest.getPageNum(), tableRequest.getPageSize());
        List<SysUserWithRole> userWithRoleList = sysUserMapperFix.findUserListWithRoles(tableRequest.getSearch());
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

    @Override
    @Transactional(readOnly = false)
    public int deleteUser(String id) {
        SysUserRoleExample example = new SysUserRoleExample();
        example.createCriteria().andSysRoleIdEqualTo(1);
        //查询系统管理员个数，<= 1 则禁止删除管理员
        List<SysUserRole> roles = sysUserRoleMapper.selectByExample(example);
        if (roles.size() < 2) {
            throw new AccountChangeException("不能删除唯一的系统管理员");
        }
        //删除用户角色关联表信息
        SysUserRoleExample example1 = new SysUserRoleExample();
        example1.createCriteria().andSysUserIdEqualTo(id);
        sysUserRoleMapper.deleteByExample(example1);
        //删除用户表用户信息
        return sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(readOnly = false)
    public int disableUser(String id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setIsDelete(1);
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(readOnly = false)
    public int enableUser(String id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setIsDelete(0);
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<SysUser> findUserByRoleName(String roleName) {
        return sysUserMapperFix.findUserByRoleName(roleName);
    }

    @Override
    public Map<String, String> findUserFilterMap() {
        return null;
    }


}
