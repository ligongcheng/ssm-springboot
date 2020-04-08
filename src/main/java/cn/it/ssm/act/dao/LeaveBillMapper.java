package cn.it.ssm.act.dao;

import cn.it.ssm.act.entity.LeaveBill;
import cn.it.ssm.act.entity.LeaveBillExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeaveBillMapper {
    long countByExample(LeaveBillExample example);

    int deleteByExample(LeaveBillExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveBill record);

    int insertOrUpdate(LeaveBill record);

    int insertOrUpdateSelective(LeaveBill record);

    int insertSelective(LeaveBill record);

    List<LeaveBill> selectByExample(LeaveBillExample example);

    LeaveBill selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveBill record, @Param("example") LeaveBillExample example);

    int updateByExample(@Param("record") LeaveBill record, @Param("example") LeaveBillExample example);

    int updateByPrimaryKeySelective(LeaveBill record);

    int updateByPrimaryKey(LeaveBill record);

    int updateBatch(List<LeaveBill> list);

    int updateBatchSelective(List<LeaveBill> list);

    int batchInsert(@Param("list") List<LeaveBill> list);

    List<LeaveBill> findByUserIdOrderByIdDesc(@Param("id") String id);
}
