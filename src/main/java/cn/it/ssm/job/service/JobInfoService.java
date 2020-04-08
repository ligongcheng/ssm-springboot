package cn.it.ssm.job.service;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.job.entity.JobInfo;

public interface JobInfoService {
    int save(JobInfo jobInfo);

    PageListVO findAll(TableRequest tableRequest);

    JobInfo findById(int id);

    int updateCronById(JobInfo jobInfo);

    int updateAllStatus(int status);

    int deleteById(Integer id);

    int updateByPrimaryKeySelective(JobInfo jobInfo);

    int updateStatusById(Integer updatedStatus, Integer id);
}
