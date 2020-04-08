package cn.it.ssm.common.util;

import cn.it.ssm.common.ExceptionHandler.AppException;
import cn.it.ssm.job.entity.JobInfo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;

@Component
public class SchedulerManager {

    // 任务调度
    @Autowired
    private Scheduler scheduler;

    public boolean addJob(JobInfo jobInfo) {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobInfo.getClassNmae());
            // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
            // JobDetail 是具体Job实例
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobInfo.getName()).build();
            // 基于表达式构建触发器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression()).inTimeZone(TimeZone.getTimeZone("GMT+8"));
            // CronTrigger表达式触发器 继承于Trigger
            // TriggerBuilder 用于构建触发器实例
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobInfo.getName())
                .withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
        return true;
    }

    /**
     * 修改某个任务的执行时间
     *
     * @throws SchedulerException
     */
    public boolean editJobCronTime(JobInfo jobInfo) {
        Date date = null;
        try {
            date = null;
            TriggerKey triggerKey = new TriggerKey(jobInfo.getName());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            String oldTime = cronTrigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(jobInfo.getCronExpression())) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobInfo.getName())
                    .withSchedule(cronScheduleBuilder).build();
                date = scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            throw new AppException(e.getMessage());
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 暂停某个任务
     *
     * @param jobInfo
     * @throws SchedulerException
     */
    public void pauseJob(JobInfo jobInfo) {
        try {
            JobKey jobKey = new JobKey(jobInfo.getName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 恢复某个任务
     *
     * @param jobInfo
     * @throws SchedulerException
     */
    public void resumeJob(JobInfo jobInfo) {
        try {
            JobKey jobKey = new JobKey(jobInfo.getName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 删除某个任务
     *
     * @param jobInfo
     * @throws SchedulerException
     */
    public void deleteJob(JobInfo jobInfo) {
        try {
            JobKey jobKey = new JobKey(jobInfo.getName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new AppException(e.getMessage());
        }
    }
}
