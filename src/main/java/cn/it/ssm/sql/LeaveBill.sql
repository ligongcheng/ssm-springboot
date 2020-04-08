-- auto Generated on 2020-04-03
-- DROP TABLE IF EXISTS ac_leave_bill;
CREATE TABLE ac_leave_bill
(
    uesr_id             VARCHAR(50) NOT NULL COMMENT '用户id',
    username            VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户名',
    leave_day           INT(11)     NOT NULL DEFAULT -1 COMMENT '请假天数',
    start_time          DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '开始时间',
    end_time            DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '结束时间',
    process_instance_id VARCHAR(50) NOT NULL DEFAULT '' COMMENT '流程实例Id',
    INDEX (username),
    INDEX (process_instance_id),
    PRIMARY KEY (uesr_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT 'ac_leave_bill';
