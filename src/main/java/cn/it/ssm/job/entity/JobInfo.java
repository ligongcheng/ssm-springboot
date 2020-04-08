package cn.it.ssm.job.entity;

import java.io.Serializable;
import java.util.Date;

public class JobInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * job id
     */
    private Integer id;
    /**
     * job name
     */
    private String name;
    /**
     * classNmae
     */
    private String classNmae;
    /**
     * job 描述
     */
    private String desc;
    /**
     * cron 表达式
     */
    private String cronExpression;
    /**
     * 0 暂停 1 运行
     */
    private Integer status;
    private Date creatTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassNmae() {
        return classNmae;
    }

    public void setClassNmae(String classNmae) {
        this.classNmae = classNmae;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public enum JobStatus {
        WORKING(1), STOP(0);
        private int code;

        JobStatus(int code) {
            this.code = code;
        }

        public int status() {
            return this.code;
        }
    }
}
