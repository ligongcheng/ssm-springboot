-- auto Generated on 2020-04-03
-- DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept
(
    dept_id     INT(11)     NOT NULL AUTO_INCREMENT COMMENT '部门id',
    parent_id   INT(11)     NOT NULL DEFAULT -1 COMMENT '父级部门名称',
    dept_name   VARCHAR(50) NOT NULL DEFAULT '' COMMENT '部门名称',
    sort        INT(11)     NOT NULL DEFAULT -1 COMMENT '排序',
    available   INT(11)     NOT NULL DEFAULT -1 COMMENT '是否可用',
    description VARCHAR(50) NOT NULL DEFAULT '' COMMENT '描述',
    create_time DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
    update_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    INDEX (dept_name),
    PRIMARY KEY (dept_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT 'sys_dept';
