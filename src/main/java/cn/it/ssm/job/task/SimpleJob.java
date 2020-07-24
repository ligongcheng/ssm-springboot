package cn.it.ssm.job.task;

import cn.it.ssm.job.dao.JobInfoMapper;
import cn.it.ssm.job.dao.JobLogMapper;
import cn.it.ssm.job.entity.JobInfo;
import cn.it.ssm.job.entity.JobLog;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * SpringBeanJobFactory 实现了AutowireCapableBeanFactory 可以实现ioc功能
 */
@DisallowConcurrentExecution
@Slf4j
@Component
public class SimpleJob extends QuartzJobBean {

    @Autowired
    private JobLogMapper jobLogMapper;

    @Autowired
    private JobInfoMapper jobInfoMapper;

    private JobLog beforeLog(JobExecutionContext context) {
        String name = context.getJobDetail().getKey().getName();
        Class<? extends Job> jobClass = context.getJobDetail().getJobClass();
        JobInfo info = new JobInfo();
        info.setName(name);
        JobInfo jobInfo = jobInfoMapper.selectByAll(info).get(0);
        List<JobLog> jobLogList = jobLogMapper.selectByJobIdOrderByIdDesc(jobInfo.getId());
        JobLog jobLog = jobLogList.size() > 0 ? jobLogList.get(0) : new JobLog();
        jobLog.setJobId(jobInfo.getId());
        jobLog.setClassName(jobInfo.getClassNmae());
        jobLog.setCreateTime(new Date());
        Integer times = jobLog.getTimes();
        if (times == null) {
            times = 0;
        }
        jobLog.setTimes(times + 1);
        jobLog.setId(null);
        return jobLog;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {

        JobLog jobLog = this.beforeLog(context);

        long start = System.currentTimeMillis();
        String name = context.getJobDetail().getKey().getName();
        // 执行任务
        try {
            jobLog.setStatus(String.format("任务[%s] 正在执行", name));
            // do work
            Thread.sleep(2000);


            jobLog.setStatus(String.format("任务[%s] 执行成功", name));

        } catch (Exception e) {
            e.printStackTrace();
            jobLog.setStatus(String.format("任务[%s] 执行异常", name));
            jobLog.setError(e.getMessage());
        } finally {
            jobLog.setDuration((int) (System.currentTimeMillis() - start));
            jobLogMapper.insertSelective(jobLog);
        }
    }
}
