package cn.it.ssm.job.service.impl;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.job.dao.JobInfoMapper;
import cn.it.ssm.job.entity.JobInfo;
import cn.it.ssm.job.service.JobInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    public int save(JobInfo jobInfo) {

        return jobInfoMapper.insert(jobInfo);
    }

    @Override
    public PageListVO findAll(TableRequest tableRequest) {
        PageHelper.startPage(tableRequest.getPageNum(), tableRequest.getPageSize());
        PageListVO pageListVO = new PageListVO();
        List<JobInfo> list = jobInfoMapper.selectByAll(null);
        PageInfo<JobInfo> pageInfo = PageInfo.of(list);
        pageListVO.setTotal(pageInfo.getTotal());
        pageListVO.setRows(pageInfo.getList());
        return pageListVO;
    }

    @Override
    public JobInfo findById(int id) {
        return jobInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateCronById(JobInfo jobInfo) {
        JobInfo old = jobInfoMapper.selectByPrimaryKey(jobInfo.getId());
        old.setCronExpression(jobInfo.getCronExpression());
        return jobInfoMapper.updateByPrimaryKeySelective(old);
    }


    @Override
    public int updateAllStatus(int status) {
        return jobInfoMapper.updateStatusById(status, null);
    }

    @Override
    public int deleteById(Integer id) {
        return jobInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(JobInfo jobInfo) {
        return jobInfoMapper.updateByPrimaryKeySelective(jobInfo);
    }

    @Override
    public int updateStatusById(Integer updatedStatus, Integer id) {
        return jobInfoMapper.updateStatusById(updatedStatus, id);
    }

}
