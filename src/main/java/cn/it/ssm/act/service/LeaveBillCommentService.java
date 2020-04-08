package cn.it.ssm.act.service;

import cn.it.ssm.act.entity.LeaveBillComment;

import java.util.List;

public interface LeaveBillCommentService {
    int save(LeaveBillComment comment);

    List<LeaveBillComment> findByProcessInstanceIdOrderByTaskIdDesc(String processInstanceId);
}
