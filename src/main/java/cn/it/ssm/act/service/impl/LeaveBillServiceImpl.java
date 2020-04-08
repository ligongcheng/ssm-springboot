package cn.it.ssm.act.service.impl;

import cn.it.ssm.act.dao.LeaveBillMapper;
import cn.it.ssm.act.entity.LeaveBill;
import cn.it.ssm.act.entity.LeaveBillExample;
import cn.it.ssm.act.service.LeaveBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class LeaveBillServiceImpl implements LeaveBillService {
    @Autowired
    LeaveBillMapper leaveBillMapper;

    @Override
    public int save(LeaveBill bill) {
        return leaveBillMapper.insert(bill);
    }

    @Override
    public LeaveBill findByProcessInstanceId(String id) {
        LeaveBillExample example = new LeaveBillExample();
        example.createCriteria().andProcessInstanceIdEqualTo(id);
        return leaveBillMapper.selectByExample(example).get(0);
    }

    @Override
    public int updateByPrimaryKey(LeaveBill bill) {
        return leaveBillMapper.updateByPrimaryKeySelective(bill);
    }

    @Override
    public LeaveBill findById(Integer id) {
        return leaveBillMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<LeaveBill> findByUserIdOrderByIdDesc(String id) {
        //LeaveBillExample example = new LeaveBillExample();
        //example.createCriteria().andUesrIdEqualTo(id);
        return leaveBillMapper.findByUserIdOrderByIdDesc(id);
    }

}
