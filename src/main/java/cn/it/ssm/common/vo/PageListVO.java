package cn.it.ssm.common.vo;

import java.io.Serializable;
import java.util.List;

public class PageListVO implements Serializable {

    private Long total;
    private List rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }


}
