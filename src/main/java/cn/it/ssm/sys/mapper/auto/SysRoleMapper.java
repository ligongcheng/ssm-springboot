package cn.it.ssm.sys.mapper.auto;


import cn.it.ssm.sys.domain.auto.SysPermission;
import cn.it.ssm.sys.domain.auto.SysRole;
import cn.it.ssm.sys.domain.auto.SysRoleExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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

    @Delete("DELETE \n" +
            "FROM\n" +
            "  sys_role_permission \n" +
            "WHERE sys_role_id = #{roleId} ")
    int deleteRolePermsByRoleId(Integer roleId);

    @Insert("INSERT INTO sys_role_permission (sys_role_id, sys_permission_id) \n" +
            "VALUES\n" +
            "  (#{roleId}, #{permId})")
    int saveRolePermsByPermId(@Param("roleId") Integer roleId, @Param("permId") Integer permId);
}
