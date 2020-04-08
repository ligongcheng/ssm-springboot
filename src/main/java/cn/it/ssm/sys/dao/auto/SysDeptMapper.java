package cn.it.ssm.sys.dao.auto;

import cn.it.ssm.sys.domain.auto.SysDept;
import cn.it.ssm.sys.domain.auto.SysDeptExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    long countByExample(SysDeptExample example);

    int deleteByExample(SysDeptExample example);

    int deleteByPrimaryKey(Integer deptId);

    int insert(SysDept record);

    int insertOrUpdate(SysDept record);

    int insertOrUpdateSelective(SysDept record);

    int insertSelective(SysDept record);

    List<SysDept> selectByExample(SysDeptExample example);

    SysDept selectByPrimaryKey(Integer deptId);

    int updateByExampleSelective(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    int updateByExample(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    int updateBatch(List<SysDept> list);

    int updateBatchSelective(List<SysDept> list);

    int batchInsert(@Param("list") List<SysDept> list);
}
