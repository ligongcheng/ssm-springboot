-- auto Generated on 2020-04-03
-- DROP TABLE IF EXISTS leave_bill_comment;
CREATE TABLE leave_bill_comment
(
    task_id             VARCHAR(50) NOT NULL COMMENT '任务id',
    process_instance_id VARCHAR(50) NOT NULL DEFAULT '' COMMENT '流程实例Id',
    assign_id           VARCHAR(50) NOT NULL DEFAULT '' COMMENT '任务签收人id',
    assign_user         VARCHAR(50) NOT NULL DEFAULT '' COMMENT '任务签收人',
    `comment`           VARCHAR(50) NOT NULL DEFAULT '' COMMENT '任务批注',
    INDEX (process_instance_id),
    INDEX (assign_id),
    INDEX (assign_user),
    PRIMARY KEY (task_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT 'leave_bill_comment';
