/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 5.5.62 : Database - ssm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE = '' */;

/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS */`ssm` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `ssm`;

/*Table structure for table `data_dict` */

DROP TABLE IF EXISTS `data_dict`;

CREATE TABLE `data_dict` (
  `id`        INT(11)     NOT NULL AUTO_INCREMENT,
  `type_name` VARCHAR(64) NOT NULL
  COMMENT '数据字典类型名称',
  `type_code` VARCHAR(64)          DEFAULT NULL
  COMMENT '数据字典类型代码',
  `ddkey`     VARCHAR(6)  NOT NULL
  COMMENT '数据键',
  `ddvalue`   VARCHAR(12) NOT NULL
  COMMENT '数据值',
  `is_show`   INT(1)      NOT NULL
  COMMENT '是否显示，1：显示；2：不显示',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8mb4
  COMMENT ='数据字典表';

/*Data for the table `data_dict` */

INSERT INTO `data_dict` (`id`, `type_name`, `type_code`, `ddkey`, `ddvalue`, `is_show`) VALUES
  (1, '性别', 'sex', '0', '女', 1),
  (2, '性别', 'sex', '1', '男', 1),
  (3, '性别', 'sex', '2', '保密', 1),
  (4, '汽车类型', 'carType', '2', '公交车', 1),
  (5, '汽车类型', 'carType', '1', '轿车', 1),
  (6, '职业', 'job', '1', 'Java开发', 1),
  (7, '职业', 'job', '2', '前端开发', 1),
  (8, '职业', 'job', '3', '大数据开发', 1),
  (9, '职业', 'job', '4', 'ios开发', 1),
  (10, '职业', 'job', '5', 'Android开发', 1),
  (11, '职业', 'job', '6', 'Linux系统工程师', 1),
  (12, '职业', 'job', '7', 'PHP开发', 1),
  (13, '职业', 'job', '8', '.net开发', 1),
  (14, '职业', 'job', '9', 'C/C++', 1),
  (15, '职业', 'job', '10', '学生', 0),
  (16, '职业', 'job', '11', '其它', 1),
  (17, '职业', 'job', '12', '全栈牛逼架构师', 1),
  (18, '汽车类型', 'carType', '3', '海陆两用', 1);

/*Table structure for table `demo_item` */

DROP TABLE IF EXISTS `demo_item`;

CREATE TABLE `demo_item` (
  `id`     VARCHAR(20)  NOT NULL,
  `name`   VARCHAR(255) NOT NULL,
  `amount` INT(11)      NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `demo_item` */

INSERT INTO `demo_item` (`id`, `name`, `amount`) VALUES
  ('170909FRA2NB7TR4', '红翼 red wing', 215000),
  ('170909FRB9DPXY5P', '红翼 9111', 210000),
  ('170909FRCAT15XGC', '红翼 875', 215000),
  ('170909FRF2P18ARP', 'cat', 185000),
  ('170909FRG6R75PZC', 'dog', 195000),
  ('170909FRHBS3K680', '马丁靴', 150000),
  ('170909FRPWA5HCPH', '天木兰 经典 船鞋', 65000),
  ('170909FRS6SBHH00', '天木兰 踢不烂', 65000),
  ('170909FRX22HKCDP', '其乐 袋鼠靴', 70000);

/*Table structure for table `sys_permission` */

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id`          INT(20)      NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `name`        VARCHAR(128) NOT NULL
  COMMENT '资源名称',
  `type`        INT(2)       NOT NULL
  COMMENT '资源类型：\r\n0：顶级根权限\r\n1：菜单,间接代表就是 isParent=true\r\n2：普通链接（按钮，link等）',
  `url`         VARCHAR(128)          DEFAULT NULL
  COMMENT '访问url地址',
  `percode`     VARCHAR(128)          DEFAULT NULL
  COMMENT '权限代码字符串',
  `parentid`    INT(11)               DEFAULT NULL
  COMMENT '父结点id\r\n为0代表根节点',
  `parentids`   VARCHAR(128)          DEFAULT NULL
  COMMENT '父结点id列表串',
  `sort`        INT(3)                DEFAULT NULL
  COMMENT '排序号',
  `available`   INT(1)       NOT NULL
  COMMENT '是否可用,1：可用，0不可用',
  `description` VARCHAR(128)          DEFAULT NULL
  COMMENT '当前资源描述',
  `create_time` DATETIME     NOT NULL,
  `update_time` DATETIME     NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `percode` (`percode`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 268
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `sys_permission` */

INSERT INTO `sys_permission` (`id`, `name`, `type`, `url`, `percode`, `parentid`, `parentids`, `sort`, `available`, `description`, `create_time`, `update_time`)
VALUES
  (237, '用户管理', 1, '/sys/user:get', 'sys:user:list', 0, NULL, 1, 1, '用户管理-菜单按钮', '2016-10-24 14:28:31',
   '2016-10-24 14:28:31'),
  (239, '用户管理-修改', 2, '/sys/user:put', 'sys:user:update', 237, NULL, 2, 1, '用户管理-修改', '2016-10-24 14:37:37', '2016-10-24 14:37:45'),
  (241, '用户管理-删除', 2, '/sys/user:delete', 'sys:user:delete', 237, NULL, 4, 1, '用户管理-删除', '2016-10-24 14:41:52', '2016-10-24 14:42:11'),
  (259, '用户管理-新增', 2, '/sys/user:post', 'sys:user:add', 237, NULL, 1, 1, '用户管理-新增', '2019-02-10 20:16:56', '2019-02-10 20:16:56'),
  (260, '角色管理', 1, '/sys/role:get', 'sys:role:list', 0, NULL, 23, 1, '角色管理', '2019-02-13 20:01:57', '2019-02-13 20:01:57'),
  (261, '角色管理-新增', 2, '/sys/role:post', 'sys:role:add', 260, NULL, 24, 1, '角色管理-新增', '2019-02-13 20:02:48', '2019-02-13 20:02:48'),
  (262, '角色管理-修改', 2, '/sys/role:put', 'sys:role:update', 260, NULL, 25, 1, '角色管理-修改', '2019-02-13 20:03:28', '2019-02-13 20:03:28'),
  (263, '角色管理-删除', 2, '/sys/role:delete', 'sys:role:delete', 260, NULL, 26, 1, '角色管理-删除', '2019-02-13 20:04:01', '2019-02-13 20:04:01'),
  (264, '权限管理', 1, '/sys/perm:get', 'sys:perm:list', 0, NULL, 3, 1, '权限管理', '2019-02-13 20:05:02', '2019-02-13 20:05:02'),
  (265, '权限管理-新增', 2, '/sys/perm:post', 'sys:perm:add', 264, NULL, 31, 1, '权限管理-新增', '2019-02-13 20:05:37', '2019-02-13 20:05:37'),
  (266, '权限管理-修改', 2, '/sys/perm:put', 'sys:perm:update', 264, NULL, 32, 1, '权限管理-修改', '2019-02-13 20:06:04', '2019-02-13 20:06:04'),
  (267, '权限管理-删除', 2, '/sys/perm:delete', 'sys:perm:delete', 264, NULL, 33, 1, '权限管理-删除', '2019-02-13 20:06:32',
   '2019-02-13 20:06:32');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id`        INT(20)      NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(128) NOT NULL,
  `available` INT(1)       NOT NULL DEFAULT '1'
  COMMENT '是否可用,1：可用，0不可用',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `sys_role` */

INSERT INTO `sys_role` (`id`, `name`, `available`) VALUES
  (1, '系统管理员', 1),
  (3, '用户管理员', 1),
  (4, '角色管理员', 1),
  (5, '权限管理员', 1),
  (7, '注册用户', 1);

/*Table structure for table `sys_role_permission` */

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `id`                INT(20) NOT NULL AUTO_INCREMENT,
  `sys_role_id`       INT(20) NOT NULL
  COMMENT '角色id',
  `sys_permission_id` INT(20) NOT NULL
  COMMENT '权限id',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 472
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `sys_role_permission` */

INSERT INTO `sys_role_permission` (`id`, `sys_role_id`, `sys_permission_id`) VALUES
  (318, 4, 260),
  (319, 4, 261),
  (320, 4, 262),
  (321, 4, 263),
  (322, 5, 264),
  (323, 5, 265),
  (324, 5, 267),
  (325, 5, 266),
  (454, 1, 237),
  (455, 1, 239),
  (456, 1, 241),
  (457, 1, 259),
  (458, 1, 260),
  (459, 1, 261),
  (460, 1, 262),
  (461, 1, 263),
  (462, 1, 264),
  (463, 1, 265),
  (464, 1, 266),
  (465, 1, 267),
  (466, 7, 237),
  (467, 3, 237),
  (468, 3, 239),
  (469, 3, 241),
  (470, 3, 259),
  (471, 3, 261);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id`              VARCHAR(32) NOT NULL,
  `username`        VARCHAR(60) NOT NULL
  COMMENT '用户名，登录名',
  `password`        VARCHAR(64) NOT NULL
  COMMENT '密码',
  `nickname`        VARCHAR(60)          DEFAULT NULL
  COMMENT '昵称',
  `age`             INT(3)               DEFAULT '0'
  COMMENT '年龄',
  `sex`             INT(1)      NOT NULL DEFAULT '2'
  COMMENT '性别\r\n0：女\r\n1：男\r\n2：保密 ',
  `job`             INT(10)              DEFAULT NULL
  COMMENT '职业类型：\r\n1：Java开发\r\n2：前端开发\r\n3：大数据开发\r\n4：ios开发\r\n5：Android开发\r\n6：Linux系统工程师\r\n7：PHP开发\r\n8：.net开发\r\n9：C/C++\r\n10：学生\r\n11：其它',
  `face_image`      VARCHAR(255)         DEFAULT NULL
  COMMENT '头像地址',
  `province`        VARCHAR(12)          DEFAULT NULL
  COMMENT '省',
  `city`            VARCHAR(12)          DEFAULT NULL
  COMMENT '市',
  `district`        VARCHAR(12)          DEFAULT NULL
  COMMENT '区',
  `address`         VARCHAR(128)         DEFAULT NULL
  COMMENT '详细地址',
  `auth_salt`       VARCHAR(32)          DEFAULT NULL
  COMMENT '用于权限的“盐”',
  `last_login_ip`   VARCHAR(20)          DEFAULT NULL
  COMMENT '最后一次登录IP',
  `last_login_time` DATETIME             DEFAULT NULL
  COMMENT '最后一次登录时间',
  `is_delete`       INT(1)      NOT NULL DEFAULT '0',
  `regist_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='系统后台用户';

/*Data for the table `sys_user` */

INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `age`, `sex`, `job`, `face_image`, `province`, `city`, `district`, `address`, `auth_salt`, `last_login_ip`, `last_login_time`, `is_delete`, `regist_time`)
VALUES
  ('89d11305c17a43d88491633c2f0aedde', 'test2', 'e332ca2aa09a4266765df24ddf3f1b31', 'test2', 22, 1, NULL, NULL, NULL,
                                       NULL, NULL, NULL, '2d73', NULL, NULL, 0, '2019-03-01 03:56:49'),
  ('b07a959029a8400388ad3cd77d804b9c', 'test', 'ee154dbc5d731b57c368a68c4e3a4590', 'test', 22, 2, NULL, NULL, NULL,
                                       NULL, NULL, NULL, '986c', '127.0.0.1', '2019-12-03 04:28:34', 0,
   '2019-03-01 02:40:28'),
  ('c247c97b47414d50b53144b80d14ade6', 'admin', '176a6743fe9b649e635e107ec02ea0f6', 'admin', 22, 1, NULL, NULL, NULL,
                                       NULL, NULL, NULL, '27a4', NULL, NULL, 0, '2019-03-01 03:57:42'),
  ('f09bd8dc71094007a6e0f7786a6bb192', 'test3', '2dbbeaf3ea5187d0cd19f8ceff1de2b6', 'test3', 22, 0, NULL, NULL, NULL,
                                       NULL, NULL, NULL, '959d', NULL, NULL, 0, '2019-03-01 03:57:05'),
  ('f6d508b253c44e54947914c905995b85', 'test1', 'cf68501d8d78202cd2c97ad8e794032f', 'test1', 22, 2, NULL, NULL, NULL,
                                       NULL, NULL, NULL, '47b7', NULL, NULL, 0, '2019-03-01 03:44:46');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id`          INT(20)     NOT NULL AUTO_INCREMENT,
  `sys_user_id` VARCHAR(32) NOT NULL,
  `sys_role_id` INT(32)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 38
  DEFAULT CHARSET = utf8mb4;

/*Data for the table `sys_user_role` */

INSERT INTO `sys_user_role` (`id`, `sys_user_id`, `sys_role_id`) VALUES
  (31, 'b07a959029a8400388ad3cd77d804b9c', 1),
  (34, 'f6d508b253c44e54947914c905995b85', 3),
  (35, '89d11305c17a43d88491633c2f0aedde', 3),
  (36, 'f09bd8dc71094007a6e0f7786a6bb192', 5),
  (37, 'c247c97b47414d50b53144b80d14ade6', 1);

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
