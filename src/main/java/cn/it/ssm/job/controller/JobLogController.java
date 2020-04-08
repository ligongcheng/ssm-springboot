package cn.it.ssm.job.controller;

import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.job.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("joblog")
public class JobLogController {

    @Autowired
    private JobLogService jobLogService;

    @RequestMapping("/joblogUI")
    public String jobLogUI() {
        return "/sys/joblog";
    }

    @GetMapping
    @ResponseBody
    public PageListVO pageQuery(TableRequest tableRequest) {
        return jobLogService.pageQuery(tableRequest);
    }

    @DeleteMapping
    @ResponseBody
    public ConResult delJobLogs(@RequestBody List<Integer> ids) {
        jobLogService.delJobLogsByIds(ids);
        return ConResult.success();
    }

}
