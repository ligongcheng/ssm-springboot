package cn.it.ssm.act.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class LeaveBill implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 用户id
     */
    private String uesrId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 状态
     * 0 未提交
     * 1 审批中
     * 2 审批通过
     * 3 未提交_已放弃
     * 4 已提交_已放弃
     */
    private Integer state;
    /**
     * 请假天数
     */
    private Integer leaveDay;
    /**
     * 请假原因
     */
    private String reason;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    /**
     * 流程实例Id
     */
    private String processInstanceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUesrId() {
        return uesrId;
    }

    public void setUesrId(String uesrId) {
        this.uesrId = uesrId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(Integer leaveDay) {
        this.leaveDay = leaveDay;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uesrId=").append(uesrId);
        sb.append(", username=").append(username);
        sb.append(", state=").append(state);
        sb.append(", leaveDay=").append(leaveDay);
        sb.append(", reason=").append(reason);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", processInstanceId=").append(processInstanceId);
        sb.append("]");
        return sb.toString();
    }
}
