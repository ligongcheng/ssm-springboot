package cn.it.ssm.job.dao;

import cn.it.ssm.job.entity.JobLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobLog record);

    int insertOrUpdate(JobLog record);

    int insertOrUpdateSelective(JobLog record);

    int insertSelective(JobLog record);

    JobLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobLog record);

    int updateByPrimaryKey(JobLog record);

    int updateBatch(List<JobLog> list);

    int batchInsert(@Param("list") List<JobLog> list);

    List<JobLog> selectByJobIdOrderByIdDesc(@Param("jobId") Integer jobId);

    List<JobLog> selectAllById(@Param("id") Integer id);

    int deleteBatchByIds(@Param("ids") List<Integer> ids);

}
