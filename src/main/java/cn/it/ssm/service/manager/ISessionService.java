package cn.it.ssm.service.manager;

import cn.it.ssm.common.vo.OnlineUserVO;

import java.util.List;

public interface ISessionService {
    List<OnlineUserVO> getOnlineUser();

    void kickOut(String id);
}
