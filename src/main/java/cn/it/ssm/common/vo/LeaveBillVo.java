package cn.it.ssm.common.vo;

import cn.it.ssm.act.entity.LeaveBill;

import java.io.Serializable;

public class LeaveBillVo extends LeaveBill implements Serializable {
    /**
     * 部署名
     */
    private String deployName;

    /**
     * 任务批注
     */
    private String comment;
    /**
     * 任务id
     */
    private String taskId;
    private Boolean assign;

    private Boolean approve;

    public String getDeployName() {
        return deployName;
    }

    public void setDeployName(String deployName) {
        this.deployName = deployName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Boolean getApprove() {
        return approve;
    }

    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }
}
