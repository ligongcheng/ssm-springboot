package cn.it.ssm.job.controller;

import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.util.SchedulerManager;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.job.entity.JobInfo;
import cn.it.ssm.job.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private SchedulerManager schedulerManager;

    @GetMapping("/jobUI")
    public String jobUI() {
        return "sys/job";
    }

    @GetMapping
    @ResponseBody
    public PageListVO getAllJob(TableRequest tableRequest) {
        PageListVO pageListVO = jobInfoService.findAll(tableRequest);
        return pageListVO;
    }

    @PostMapping
    @ResponseBody
    public ConResult addJob(@RequestBody JobInfo jobInfo) {
        jobInfo.setStatus(1);
        jobInfo.setCreatTime(new Date());
        schedulerManager.addJob(jobInfo);
        jobInfoService.save(jobInfo);
        return ConResult.success();
    }

    @PutMapping("/cron")
    @ResponseBody
    public ConResult editJobCron(@RequestBody JobInfo jobInfo) {
        JobInfo info = jobInfoService.findById(jobInfo.getId());
        info.setCronExpression(jobInfo.getCronExpression());
        schedulerManager.editJobCronTime(info);
        jobInfoService.updateCronById(info);
        return ConResult.success();
    }

    @PutMapping("/status")
    @ResponseBody
    public ConResult changeJobStatus(@RequestBody JobInfo jobInfo) {
        JobInfo info = jobInfoService.findById(jobInfo.getId());
        info.setStatus(jobInfo.getStatus());
        if (jobInfo.getStatus() == JobInfo.JobStatus.STOP.status()) {
            schedulerManager.pauseJob(info);
        } else {
            schedulerManager.resumeJob(info);
        }
        jobInfoService.updateStatusById(info.getStatus(), info.getId());
        return ConResult.success();
    }

    @PutMapping("/status/all/pause")
    @ResponseBody
    public ConResult pauseAllJobs() {
        schedulerManager.pauseAllJob();
        jobInfoService.updateAllStatus(JobInfo.JobStatus.STOP.status());
        return ConResult.success();
    }

    @PutMapping("/status/all/resume")
    @ResponseBody
    public ConResult resumeAllJobs() {
        schedulerManager.resumeAllJob();
        jobInfoService.updateAllStatus(JobInfo.JobStatus.WORKING.status());
        return ConResult.success();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ConResult delJob(@PathVariable Integer id) {
        JobInfo info = jobInfoService.findById(id);
        schedulerManager.deleteJob(info);
        jobInfoService.deleteById(info.getId());
        return ConResult.success();
    }


}
