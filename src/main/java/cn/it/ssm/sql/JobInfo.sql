-- auto Generated on 2020-03-24
-- DROP TABLE IF EXISTS t_job_info;
CREATE TABLE t_job_info
(
    id              INT(11)     NOT NULL AUTO_INCREMENT COMMENT 'job id',
    `name`          VARCHAR(50) NOT NULL COMMENT 'job name',
    class_nmae      VARCHAR(50) NOT NULL COMMENT 'classNmae',
    `desc`          VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'job 描述',
    cron_expression VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'cron 表达式',
    `status`        INT(2)      NOT NULL COMMENT '0 暂停 1 运行',
    INDEX (id),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT 't_job_info';
