package cn.it.ssm.job.service;

import cn.it.ssm.common.vo.PageListVO;
import cn.it.ssm.common.vo.TableRequest;

import java.util.List;

public interface JobLogService {

    PageListVO pageQuery(TableRequest request);

    int delJobLogsByIds(List<Integer> ids);
}
