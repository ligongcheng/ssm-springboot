package cn.it.ssm.common.entity;

import cn.it.ssm.act.entity.LeaveBill;
import cn.it.ssm.act.entity.LeaveBillComment;

import java.io.Serializable;
import java.util.List;

public class LeaveBillEntity extends LeaveBill implements Serializable {
    private static final long serialVersionUID = 4692572867650957889L;

    private List<LeaveBillComment> comments;

    public List<LeaveBillComment> getComments() {
        return comments;
    }

    public void setComments(List<LeaveBillComment> comments) {
        this.comments = comments;
    }
}
