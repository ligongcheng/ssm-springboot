package cn.it.ssm.act.service.impl;

import cn.it.ssm.act.dao.LeaveBillCommentMapper;
import cn.it.ssm.act.entity.LeaveBillComment;
import cn.it.ssm.act.service.LeaveBillCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class LeaveBillCommentServiceImpl implements LeaveBillCommentService {
    @Autowired
    private LeaveBillCommentMapper leaveBillCommentMapper;

    @Override
    public int save(LeaveBillComment comment) {
        return leaveBillCommentMapper.insert(comment);
    }

    @Override
    public List<LeaveBillComment> findByProcessInstanceIdOrderByTaskIdDesc(String processInstanceId) {
        return leaveBillCommentMapper.findByProcessInstanceIdOrderByTaskIdDesc(processInstanceId);
    }
}
