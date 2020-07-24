package cn.it.ssm.act.controller;

import cn.it.ssm.common.util.SpringContextUtils;
import cn.it.ssm.sys.domain.auto.SysUser;
import cn.it.ssm.sys.service.impl.UserServiceImpl;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveBillListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(LeaveBillListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String query = "";
        String taskName = delegateTask.getName();
        log.debug("当前任务名为[{}]", taskName);
        if ("提交申请".equals(taskName)) return;
        if ("领导审批".equals(taskName)) query = "部门主管";
        if ("HR审批".equals(taskName)) query = "HR";
        Map<String, Object> map = new HashMap<>();
        UserServiceImpl userService = SpringContextUtils.getBean(UserServiceImpl.class);
        List<SysUser> userList = userService.findUserByRoleName(query.equals("") ? taskName : query);
        List<String> stringList = userList.stream().map(SysUser::getUsername).collect(Collectors.toList());
        delegateTask.addCandidateUsers(stringList);
    }
}
