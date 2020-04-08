package cn.it.ssm.act.entity;

import java.io.Serializable;

public class LeaveBillComment implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 流程实例Id
     */
    private String processInstanceId;
    /**
     * 任务签收人id
     */
    private String assignId;
    /**
     * 任务签收人
     */
    private String assignUser;
    /**
     * 1同意或者0不同意
     */
    private Integer approve;
    /**
     * 任务批注
     */
    private String comment;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", taskId=").append(taskId);
        sb.append(", taskName=").append(taskName);
        sb.append(", processInstanceId=").append(processInstanceId);
        sb.append(", assignId=").append(assignId);
        sb.append(", assignUser=").append(assignUser);
        sb.append(", approve=").append(approve);
        sb.append(", comment=").append(comment);
        sb.append("]");
        return sb.toString();
    }
}
