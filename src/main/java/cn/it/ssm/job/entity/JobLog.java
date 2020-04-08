package cn.it.ssm.job.entity;

import java.io.Serializable;
import java.util.Date;

public class JobLog implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * log Id
     */
    private Integer id;
    /**
     * jobId
     */
    private Integer jobId;
    /**
     * beanName
     */
    private String className;
    /**
     * status
     */
    private String status;
    /**
     * error
     */
    private String error;
    /**
     * times
     */
    private Integer times;
    /**
     * createTime
     */
    private Date createTime;
    /**
     * 执行时间 毫秒
     */
    private Integer duration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
