package cn.it.ssm.act.service;

import cn.it.ssm.act.entity.LeaveBill;

import java.util.List;

public interface LeaveBillService {
    int save(LeaveBill bill);

    LeaveBill findByProcessInstanceId(String id);

    int updateByPrimaryKey(LeaveBill bill);

    LeaveBill findById(Integer id);

    List<LeaveBill> findByUserIdOrderByIdDesc(String id);

}
