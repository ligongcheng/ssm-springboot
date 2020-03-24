package cn.it.ssm.sys.service;

import cn.it.ssm.common.vo.OnlineUserVO;

import java.util.List;

public interface SessionService {
    List<OnlineUserVO> getOnlineUser();

    void kickOut(String id);
}
