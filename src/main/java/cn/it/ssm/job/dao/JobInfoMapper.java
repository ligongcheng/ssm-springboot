package cn.it.ssm.job.dao;

import cn.it.ssm.job.entity.JobInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobInfo record);

    int insertSelective(JobInfo record);

    JobInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobInfo record);

    int updateByPrimaryKey(JobInfo record);

    List<JobInfo> selectByAll(JobInfo jobInfo);

    int updateStatusById(@Param("updatedStatus") Integer updatedStatus, @Param("id") Integer id);


}
