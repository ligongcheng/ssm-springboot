package cn.it.ssm.act.dao;

import cn.it.ssm.act.entity.LeaveBillComment;
import cn.it.ssm.act.entity.LeaveBillCommentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LeaveBillCommentMapper {
    long countByExample(LeaveBillCommentExample example);

    int deleteByExample(LeaveBillCommentExample example);

    int deleteByPrimaryKey(String taskId);

    int insert(LeaveBillComment record);

    int insertOrUpdate(LeaveBillComment record);

    int insertOrUpdateSelective(LeaveBillComment record);

    int insertSelective(LeaveBillComment record);

    List<LeaveBillComment> selectByExample(LeaveBillCommentExample example);

    LeaveBillComment selectByPrimaryKey(String taskId);

    int updateByExampleSelective(@Param("record") LeaveBillComment record, @Param("example") LeaveBillCommentExample example);

    int updateByExample(@Param("record") LeaveBillComment record, @Param("example") LeaveBillCommentExample example);

    int updateByPrimaryKeySelective(LeaveBillComment record);

    int updateByPrimaryKey(LeaveBillComment record);

    int updateBatch(List<LeaveBillComment> list);

    int batchInsert(@Param("list") List<LeaveBillComment> list);


    List<LeaveBillComment> findByProcessInstanceIdOrderByTaskIdDesc(@Param("processInstanceId") String processInstanceId);
}
