package cn.it.ssm.job.service.impl;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.job.dao.JobInfoMapper;
import cn.it.ssm.job.dao.JobLogMapper;
import cn.it.ssm.job.entity.JobLog;
import cn.it.ssm.job.service.JobLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobLogServiceImpl implements JobLogService {

    @Autowired
    private JobLogMapper jobLogMapper;

    @Autowired
    private JobInfoMapper jobInfoMapper;


    @Override
    public PageListVO pageQuery(TableRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        PageListVO pageListVO = new PageListVO();
        List<JobLog> list = jobLogMapper.selectAllById(null);
        PageInfo<JobLog> pageInfo = new PageInfo<>(list);
        pageListVO.setTotal(pageInfo.getTotal());
        pageListVO.setRows(pageInfo.getList());
        return pageListVO;
    }

    @Override
    public int delJobLogsByIds(List<Integer> ids) {
        return jobLogMapper.deleteBatchByIds(ids);
    }


}
