package cn.it.ssm.act.controller;

import cn.it.ssm.act.entity.LeaveBill;
import cn.it.ssm.act.service.LeaveBillService;
import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.vo.LeaveBillVo;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.sys.controller.BaseController;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("act")
public class TaskController extends BaseController {
    @Autowired
    private LeaveBillService leaveBillService;

    @RequestMapping("taskUI")
    public String taskUI() {
        return "/sys/task";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageListVO findTask(TableRequest tableRequest) {
        int first = tableRequest.getPageSize() * (tableRequest.getPageNum() - 1);
        int max = tableRequest.getPageSize();
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(getCurrentUser().getUsername())
            .orderByTaskCreateTime().desc();
        List<Task> taskList = query.listPage(first, max);
        long count = query.count();
        List<LeaveBillVo> bills = new ArrayList<>();
        for (Task task : taskList) {
            // task已有办理人，但办理人不是当前用户 跳过
            if (task.getAssignee() != null && !task.getAssignee().equals(getCurrentUser().getUsername())) {
                continue;
            }
            LeaveBillVo vo = new LeaveBillVo();
            vo.setAssign(task.getAssignee() != null);
            vo.setTaskId(task.getId());
            String name = repositoryService.getProcessDefinition(task.getProcessDefinitionId()).getName();
            LeaveBill bill = leaveBillService.findByProcessInstanceId(task.getProcessInstanceId());
            vo.setDeployName(name);
            vo.setUsername(bill.getUsername());
            vo.setState(bill.getState());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            bills.add(vo);
        }
        PageListVO pageListVO = new PageListVO();
        pageListVO.setTotal(count);
        pageListVO.setRows(bills);
        return pageListVO;
    }

    @GetMapping("/accept")
    @ResponseBody
    public ConResult acquireTask(LeaveBillVo leaveBillVo) {
        taskService.claim(leaveBillVo.getTaskId(), getCurrentUser().getUsername());
        return ConResult.success().addMsg("签收任务成功");
    }
}
