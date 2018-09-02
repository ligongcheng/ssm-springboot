package cn.it.ssm.mapper.auto;


import cn.it.ssm.domain.auto.DemoItem;
import cn.it.ssm.domain.auto.DemoItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DemoItemMapper {
    int countByExample(DemoItemExample example);

    int deleteByExample(DemoItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(DemoItem record);

    int insertSelective(DemoItem record);

    List<DemoItem> selectByExample(DemoItemExample example);

    DemoItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") DemoItem record, @Param("example") DemoItemExample example);

    int updateByExample(@Param("record") DemoItem record, @Param("example") DemoItemExample example);

    int updateByPrimaryKeySelective(DemoItem record);

    int updateByPrimaryKey(DemoItem record);
}