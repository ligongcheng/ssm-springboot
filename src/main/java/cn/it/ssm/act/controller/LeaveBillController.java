package cn.it.ssm.act.controller;

import cn.it.ssm.act.entity.LeaveBill;
import cn.it.ssm.act.entity.LeaveBillComment;
import cn.it.ssm.act.service.LeaveBillCommentService;
import cn.it.ssm.act.service.LeaveBillService;
import cn.it.ssm.common.entity.ConResult;
import cn.it.ssm.common.entity.LeaveBillCommentState;
import cn.it.ssm.common.entity.LeaveBillState;
import cn.it.ssm.common.util.ActGeneratorImgUtils;
import cn.it.ssm.common.vo.LeaveBillVo;
import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;
import cn.it.ssm.sys.controller.BaseController;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.service.RoleService;
import cn.it.ssm.sys.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("act")
public class LeaveBillController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private LeaveBillService leaveBillService;

    @Autowired
    private LeaveBillCommentService leaveBillCommentService;

    @RequestMapping("leaveUI")
    public String leaveUI() {
        return "/sys/leave";
    }

    @PostMapping("deploy")
    @ResponseBody
    public ConResult deploy(LeaveBillVo leaveBillVo, MultipartFile file) throws IOException {
        DeploymentBuilder deployment = repositoryService.createDeployment();
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        Deployment deploy = deployment.name(leaveBillVo.getDeployName()).addZipInputStream(zipInputStream).deploy();
        return ConResult.success().addMsg("部署成功");
    }

    @PostMapping("leave/apply")
    @ResponseBody
    public ConResult startLeaveBill(@RequestBody LeaveBillVo leaveBillVo) throws InvocationTargetException, IllegalAccessException {
        SysUser user = getCurrentUser();
        LeaveBill leaveBill = new LeaveBill();
        BeanUtils.copyProperties(leaveBill, leaveBillVo);
        leaveBill.setUesrId(user.getId());
        leaveBill.setUsername(user.getUsername());
        leaveBill.setState(LeaveBillState.NOT_COMMIT);
        leaveBillService.save(leaveBill);
        return ConResult.success().addMsg("填写请假单成功");
    }

    @GetMapping("leave/list")
    @ResponseBody
    public PageListVO leaveList(TableRequest tableRequest) {
        PageHelper.startPage(tableRequest.getPageNum(), tableRequest.getPageSize());
        List<LeaveBill> billList = leaveBillService.findByUserIdOrderByIdDesc(getCurrentUser().getId());
        PageInfo<LeaveBill> pageInfo = PageInfo.of(billList);
        PageListVO vo = new PageListVO();
        vo.setTotal(pageInfo.getTotal());
        vo.setRows(pageInfo.getList());
        return vo;
    }

    @GetMapping("leave/{id}")
    @ResponseBody
    public ConResult findLeaveBillByTaskId(@PathVariable String id) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        LeaveBill bill = leaveBillService.findByProcessInstanceId(task.getProcessInstanceId());
        List<LeaveBillComment> leaveBillComments = leaveBillCommentService.findByProcessInstanceIdOrderByTaskIdDesc(task.getProcessInstanceId());
        return ConResult.success().addData("leaveBill", bill).addData("leaveBillComment", leaveBillComments);
    }

    @PostMapping("leave/submit")
    @ResponseBody
    public ConResult submitLeaveBill(LeaveBill leaveBill) {
        LeaveBill bill = leaveBillService.findById(leaveBill.getId());
        identityService.setAuthenticatedUserId(getCurrentUser().getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("user", getCurrentUser().getUsername());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave_bill", map);
        String processInstanceId = processInstance.getProcessInstanceId();
        bill.setProcessInstanceId(processInstanceId);
        bill.setState(LeaveBillState.DEALING);
        leaveBillService.updateByPrimaryKey(bill);
        return ConResult.success().addMsg("申请已提交");
    }

    @PutMapping("leave/cancel")
    @ResponseBody
    public ConResult cancelLeaveBill(LeaveBill leaveBill) {
        String processInstanceId = leaveBill.getProcessInstanceId();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (pi == null) {
            //该流程实例已经完成了
            historyService.deleteHistoricProcessInstance(processInstanceId);
        } else {
            //该流程实例未结束的
            runtimeService.deleteProcessInstance(processInstanceId, "");
            historyService.deleteHistoricProcessInstance(processInstanceId);//(顺序不能换)
        }
        leaveBill.setState(LeaveBillState.GIVE_UP);
        leaveBillService.updateByPrimaryKey(leaveBill);
        return ConResult.success().addMsg("申请已取消");
    }


    @PostMapping("leave/deal")
    @ResponseBody
    public ConResult finishTask(LeaveBillVo leaveBillVo) throws InvocationTargetException, IllegalAccessException {
        List<Task> taskList = taskService.createTaskQuery().taskId(leaveBillVo.getTaskId()).list();
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(taskList)) {
            Task task = taskList.get(0);
            map.put("approve", leaveBillVo.getApprove());
            taskService.complete(task.getId(), map);
            // 更新leavebill状态
            if (runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().size() == 0) {
                LeaveBill bill = leaveBillService.findByProcessInstanceId(task.getProcessInstanceId());
                if (leaveBillVo.getApprove()) {
                    bill.setState(LeaveBillState.COMPLETE);
                } else {
                    bill.setState(LeaveBillState.COMMIT_GIVE_UP);
                }
                leaveBillService.updateByPrimaryKey(bill);
            }

            LeaveBillComment comment = new LeaveBillComment();
            BeanUtils.copyProperties(comment, leaveBillVo);
            comment.setTaskName(task.getName());
            comment.setApprove(leaveBillVo.getApprove() ? LeaveBillCommentState.AGREE : LeaveBillCommentState.DISAGREE);
            comment.setProcessInstanceId(task.getProcessInstanceId());
            comment.setAssignId(getCurrentUser().getId());
            comment.setAssignUser(getCurrentUser().getUsername());
            leaveBillCommentService.save(comment);
        }
        return ConResult.success().addMsg("任务完成");
    }

    @GetMapping("leave/img")
    public void queryProPlan(String processInstanceId, HttpServletResponse response) throws IOException {
        ActGeneratorImgUtils.getFlowImgByInstanceIdV6(processInstanceId, response.getOutputStream());
    }
}
