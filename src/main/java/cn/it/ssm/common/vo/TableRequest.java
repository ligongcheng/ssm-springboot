package cn.it.ssm.common.vo;

import java.io.Serializable;

public class TableRequest implements Serializable {

    private Integer pageSize;
    private Integer pageNum;
    private String search;


    public TableRequest() {
    }

    public TableRequest(Integer pageSize, Integer pageNum, String search) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.search = search;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
