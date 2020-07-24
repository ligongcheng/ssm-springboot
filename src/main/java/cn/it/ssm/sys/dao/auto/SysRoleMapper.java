package cn.it.ssm.sys.dao.auto;


import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysRoleExample;
import cn.it.ssm.sys.domain.auto.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int countByExample(SysRoleExample example);

    int deleteByExample(SysRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    List<SysRole> selectByExample(SysRoleExample example);

    SysRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysRole record, @Param("example") SysRoleExample example);

    int updateByExample(@Param("record") SysRole record, @Param("example") SysRoleExample example);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysPermission> findRolePermsByRoleId(SysRole sysRole);

    int deleteRolePermsByRoleId(@Param("roleId") Integer roleId);

    int saveRolePermsByPermId(@Param("roleId") Integer roleId, @Param("permId") Integer permId);

    List<SysUser> findUsersByRoleId(@Param("roleId") Integer roleId);
}
