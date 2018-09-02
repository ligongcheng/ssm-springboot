package cn.it.ssm.mapper.auto;


import cn.it.ssm.domain.auto.SysPermission;
import cn.it.ssm.domain.auto.SysRole;
import cn.it.ssm.domain.auto.SysRoleExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("SELECT \n" +
            "  p.* \n" +
            "FROM\n" +
            "  sys_role_permission rp \n" +
            "  LEFT JOIN sys_permission p \n" +
            "    ON rp.sys_permission_id = p.id \n" +
            "WHERE rp.sys_role_id = #{id} AND p.id IS NOT NULL")
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