package cn.it.ssm.common.tree;

import java.io.Serializable;
import java.util.List;

public class PermNode implements Serializable {

    private Integer id;

    private String text;

    private String state;

    private Integer parentId;

    private List<PermNode> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<PermNode> getChildren() {
        return children;
    }

    public void setChildren(List<PermNode> children) {
        this.children = children;
    }

}
